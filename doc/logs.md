为kafka供应的zookeeper的port从2181改成2182

pom.xml中 mainClass测试的时候改掉了
<transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
      <mainClass>erecommender.HBaseWriteStreamExample</mainClass>
</transformer>
