package wanion.avaritiaddons.common;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import java.io.File;

public final class Reference {

    public static final String MOD_ID = "avaritiaddons";
    public static final String MOD_NAME = "Avaritiaddons";
    public static final String MOD_VERSION = "GRADLETOKEN_VERSION";
    public static final String DEPENDENCIES = "required-after:Avaritia;required-after:wanionlib@[1.7.10-1.2,)";
    public static final String CLIENT_PROXY = "wanion.avaritiaddons.client.ClientProxy";
    public static final String SERVER_PROXY = "wanion.avaritiaddons.CommonProxy";
    public static final char SLASH = File.separatorChar;

    private Reference() {}
}
