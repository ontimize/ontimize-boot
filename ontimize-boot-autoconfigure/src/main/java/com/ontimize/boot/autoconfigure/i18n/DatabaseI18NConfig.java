package com.ontimize.boot.autoconfigure.i18n;

/**
 * @author <a href="faustino.lage@imatia.com">Faustino Lage Rego</a>
 */
public class DatabaseI18NConfig {

    private String refBundleRepository;
    private String bundleKeyColumn;
    private String bundleClassNameColumn;
    private String bundleDescriptionColumn;
    private String refBundleValueRepository;
    private String bundleValueTextKeyColumn;
    private String bundleValueKeyColumn;

    public String getRefBundleRepository() {
        return refBundleRepository;
    }

    public void setRefBundleRepository(String refBundleRepository) {
        this.refBundleRepository = refBundleRepository;
    }

    public String getBundleKeyColumn() {
        return bundleKeyColumn;
    }

    public void setBundleKeyColumn(String bundleKeyColumn) {
        this.bundleKeyColumn = bundleKeyColumn;
    }

    public String getBundleClassNameColumn() {
        return bundleClassNameColumn;
    }

    public void setBundleClassNameColumn(String bundleClassNameColumn) {
        this.bundleClassNameColumn = bundleClassNameColumn;
    }

    public String getBundleDescriptionColumn() {
        return bundleDescriptionColumn;
    }

    public void setBundleDescriptionColumn(String bundleDescriptionColumn) {
        this.bundleDescriptionColumn = bundleDescriptionColumn;
    }

    public String getRefBundleValueRepository() {
        return refBundleValueRepository;
    }

    public void setRefBundleValueRepository(String refBundleValueRepository) {
        this.refBundleValueRepository = refBundleValueRepository;
    }

    public String getBundleValueTextKeyColumn() {
        return bundleValueTextKeyColumn;
    }

    public void setBundleValueTextKeyColumn(String bundleValueTextKeyColumn) {
        this.bundleValueTextKeyColumn = bundleValueTextKeyColumn;
    }

    public String getBundleValueKeyColumn() {
        return bundleValueKeyColumn;
    }

    public void setBundleValueKeyColumn(String bundleValueKeyColumn) {
        this.bundleValueKeyColumn = bundleValueKeyColumn;
    }
}
