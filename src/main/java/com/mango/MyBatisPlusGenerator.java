package com.mango;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

// 代码生成器
class MyBatisPlusGenerator {

    public static void main(String[] args) {

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/test/java");
        globalConfig.setFileOverride(true);
        globalConfig.setIdType(IdType.INPUT);
        globalConfig.setServiceName("%sService");
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setAuthor("kem");
        globalConfig.setOpen(false);
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/mango?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("123456");
        dataSourceConfig.setDbType(DbType.MYSQL);
        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setControllerMappingHyphenStyle(true);
        // 需要导入的表的名称
        String[] sysTables = {"sys_user", "sys_user_ext", "sys_user_token", "sys_role", "sys_resource", "sys_menu_function", "sys_user_has_sys_role", "sys_role_has_sys_resource", "sys_role_has_sys_menu_function", "sys_dictionary", "sys_file", "sys_captcha", "sys_config", "sys_error_log", "sys_login_log", "sys_operate_log", "sys_message", "sys_task", "sys_task_log", "sys_mail"};
        strategyConfig.setInclude(sysTables);
        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.mango.sys");
        // Dao层的文件
        packageConfig.setMapper("mapper");
        // service层的文件
        packageConfig.setService("service");
        // controller层的文件
        packageConfig.setController("controller");
        // 实体类的文件
        packageConfig.setEntity("entity");
        // xml的文件
        packageConfig.setXml("mapper");
        // 注入自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.setCfg(injectionConfig);
        // 执行操作
        autoGenerator.execute();
    }
}
