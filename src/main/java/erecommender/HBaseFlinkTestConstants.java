package erecommender;

import org.apache.flink.configuration.ConfigConstants;

class HBaseFlinkTestConstants {

    static final byte[] CF_SOME = "someCf".getBytes(ConfigConstants.DEFAULT_CHARSET);
    static final byte[] Q_SOME = "someQual".getBytes(ConfigConstants.DEFAULT_CHARSET);
    static final String TEST_TABLE_NAME = "test-table";
    static final String TMP_DIR = "/tmp/test";

}