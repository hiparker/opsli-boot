package org.opsli.plugins.generator.factory;


/**
 * 代码生成器工厂
 *
 * @author Parker
 * @date 2021年6月1日19:10:36
 */
public final class GeneratorFactory {

    public static String getJavaHeadAnnotation(){
        return "/**\n"+
                " * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com\n"+
                " * <p>\n"+
                " * Licensed under the Apache License, Version 2.0 (the \"License\"); you may not\n"+
                " * use this file except in compliance with the License. You may obtain a copy of\n"+
                " * the License at\n"+
                " * <p>\n"+
                " * http://www.apache.org/licenses/LICENSE-2.0\n"+
                " * <p>\n"+
                " * Unless required by applicable law or agreed to in writing, software\n"+
                " * distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT\n"+
                " * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the\n"+
                " * License for the specific language governing permissions and limitations under\n"+
                " * the License.\n"+
                " */\n";
    }


    // ========================

    public GeneratorFactory(){}
}
