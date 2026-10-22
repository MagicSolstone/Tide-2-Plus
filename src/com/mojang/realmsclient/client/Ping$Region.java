package com.mojang.realmsclient.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum Ping$Region {
   US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"),
   US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"),
   US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"),
   EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"),
   AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"),
   AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"),
   AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"),
   SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com");

   private final String name;
   private final String endpoint;

   private Ping$Region(final String name, final String endpoint) {
      this.name = name;
      this.endpoint = endpoint;
   }
}
