"""Minimal async Source RCON client that handles multi-packet responses.

Minecraft splits long responses (e.g. `data get entity`) across several
packets of up to 4096 characters. When a reply fills a whole packet we send
a harmless sentinel packet; the server answers it only after the rest of the
reply, so we read until the sentinel's id comes back.
"""
from __future__ import annotations

import asyncio
import struct

AUTH = 3
COMMAND = 2
SENTINEL_TYPE = 0  # unknown type: vanilla replies "Unknown request ..."
MAX_CHUNK = 4000  # vanilla splits replies into 4096-character packets; anything near that may continue


class RconError(Exception):
    pass


class Rcon:
    def __init__(self, host: str, port: int, password: str, timeout: float = 4.0):
        self.host, self.port, self.password, self.timeout = host, port, password, timeout
        self._reader: asyncio.StreamReader | None = None
        self._writer: asyncio.StreamWriter | None = None
        self._req = 0
        self._lock = asyncio.Lock()

    async def _send(self, ptype: int, body: str) -> int:
        self._req = (self._req + 1) % 2_000_000_000 or 1
        data = body.encode("utf-8") + b"\x00\x00"
        packet = struct.pack("<ii", self._req, ptype) + data
        assert self._writer
        self._writer.write(struct.pack("<i", len(packet)) + packet)
        await self._writer.drain()
        return self._req

    async def _recv(self) -> tuple[int, int, bytes]:
        assert self._reader
        raw_len = await self._reader.readexactly(4)
        (length,) = struct.unpack("<i", raw_len)
        payload = await self._reader.readexactly(length)
        req_id, ptype = struct.unpack("<ii", payload[:8])
        return req_id, ptype, payload[8:-2]

    async def connect(self) -> None:
        try:
            self._reader, self._writer = await asyncio.wait_for(
                asyncio.open_connection(self.host, self.port), self.timeout
            )
        except ConnectionRefusedError as e:
            raise RconError(f"Nothing is listening for RCON at {self.host}:{self.port}. Check that this port is "
                            f"published from the Crafty container and that RCON is enabled, or set the RCON host "
                            f"in server settings") from e
        except (OSError, asyncio.TimeoutError) as e:
            raise RconError(f"Can't reach RCON at {self.host}:{self.port} ({e})") from e
        rid = await self._send(AUTH, self.password)
        while True:
            got_id, ptype, _ = await asyncio.wait_for(self._recv(), self.timeout)
            if got_id == -1:
                await self.close()
                raise RconError("RCON password rejected")
            if got_id == rid and ptype == 2:  # AUTH_RESPONSE
                return

    async def command(self, cmd: str) -> str:
        async with self._lock:
            if self._writer is None or self._writer.is_closing():
                await self.connect()
            try:
                return await asyncio.wait_for(self._command(cmd), self.timeout * 2)
            except (OSError, asyncio.IncompleteReadError, asyncio.TimeoutError) as e:
                await self.close()
                raise RconError(f"RCON command failed: {e}") from e

    async def _command(self, cmd: str) -> str:
        # Vanilla's RCON server parses exactly one packet per socket read and drops
        # the connection if two arrive together, so never send packets back to back:
        # send the command, wait for its first reply packet, and only then (if the
        # reply might continue) send the sentinel.
        cid = await self._send(COMMAND, cmd)
        chunks: list[bytes] = []
        while True:
            got_id, _ptype, body = await self._recv()
            if got_id == cid:
                chunks.append(body)
                break
        if len(chunks[0]) < MAX_CHUNK:
            return chunks[0].decode("utf-8", errors="replace")
        # A full-size packet means more may follow. The server finishes writing the whole
        # reply before it reads our next packet, so the sentinel's answer marks the end.
        sid = await self._send(SENTINEL_TYPE, "")
        while True:
            got_id, _ptype, body = await self._recv()
            if got_id == sid:
                break
            if got_id == cid:
                chunks.append(body)
        return b"".join(chunks).decode("utf-8", errors="replace")

    async def close(self) -> None:
        if self._writer:
            try:
                self._writer.close()
                await self._writer.wait_closed()
            except Exception:
                pass
        self._reader = self._writer = None
