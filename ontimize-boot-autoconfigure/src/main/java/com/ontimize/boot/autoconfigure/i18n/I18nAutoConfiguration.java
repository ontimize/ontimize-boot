package com.ontimize.boot.autoconfigure.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontimize.jee.common.services.i18n.II18nService;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.services.i18n.DatabaseI18nEngine;
import com.ontimize.jee.server.services.i18n.I18nConfiguration;
import com.ontimize.jee.server.services.i18n.I18nServiceImpl;

/**
 * @author <a href="faustino.lage@imatia.com">Faustino Lage Rego</a>
 */
@Configuration
@ConditionalOnProperty(name = "ontimize.i18n.engine", matchIfMissing = false)
public class I18nAutoConfiguration {

    @Autowired
    private ApplicationContext appContext;

    @Bean("I18nService")
    public II18nService i18nService(){
        return new I18nServiceImpl();
    }

    @Bean
    public I18nConfiguration i18nConfiguration() throws Exception {
        I18nConfiguration i18nConfiguration = new I18nConfiguration();
        i18nConfiguration.setEngine(this.i18Engine());
        return i18nConfiguration;
    }

    @Bean
    public II18nService i18Engine() throws Exception {
        DatabaseI18nEngine engine = new DatabaseI18nEngine();
        engine.setDaoBundles(appContext.getBean(databasei18nConfig().getRefBundleRepository(), IOntimizeDaoSupport.class));
        engine.setBundleKeyColumn(databasei18nConfig().getBundleKeyColumn());
        engine.setBundleClassNameColumn(databasei18nConfig().getBundleClassNameColumn());
        engine.setBundleDescriptionColumn(databasei18nConfig().getBundleDescriptionColumn());

        engine.setDaoBundleValues(appContext.getBean(databasei18nConfig().getRefBundleValueRepository(), IOntimizeDaoSupport.class));
        engine.setBundleValuesTextKeyColumn(databasei18nConfig().getBundleValueTextKeyColumn());
        engine.setBundleValuesKeyColumn(databasei18nConfig().getBundleValueKeyColumn());
        return engine;
    }

    @Bean
    @ConfigurationProperties(prefix = "ontimize.i18n")
    public DatabaseI18NConfig databasei18nConfig() {
        return new DatabaseI18NConfig();
    }
}
