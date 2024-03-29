# deploy-inc-plugin
deploy-inc-plugin 是增量打包的maven plugin，在maven module 非常多的项目中可以减少未改变的module编译。
并且可以增加打包依赖jar。

---
## 介绍
插件分为3个goal  

- persistence-inc `打包的信息存入临时文件`

- build-inc `增量打包，并且包括打包的信息存入临时文件` 增量打包更具打包的正式文件和本地信息对比得到

- publish-inc `临时文件存入正式文件`

- clearCache 清除除增量打包文件，清除之后打包就全量包

## 使用

1. 给打包的pom文件中绑定`persistence-inc`
```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.nouk.maven.plugin</groupId>
      <artifactId>deploy-inc-plugin</artifactId>
      <version>1.0-SNAPSHOT</version>
      <scope>system</scope>
      <systemPath>${project.basedir}/libs/deploy-inc-plugin-1.0-SNAPSHOT</systemPath>
    </dependency>
  </dependencies>  
</dependencyManagement>
<plugin>
    <groupId>org.nouk.maven.plugin</groupId>
    <artifactId>deploy-inc-plugin</artifactId>
    <version>1.0.4</version>
    <executions>
        <execution>
            <!-- 自定义 -->
            <id>persistence-publish</id>
            <phase>install</phase>
            <goals>
                <!-- 只执行一次 -->
                <goal>persistence-inc</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
2. 第一次打全量包包，`deploy-inc:build-inc -DmustDownloadJars=vdpub-auth,vdpub-common -f pom.xml` 参数DmustDownloadJars是不管jar的版本是否改变都会去maven本地仓库复制。

3. 发完完成之后，`deploy-inc:publish-inc -f pom.xml`把临时文件考到正式文件中。

> 第一次要全量包步骤 1 -> 3   
> 之后打增量包步骤 2 -> 3

### 打包全量包的参数
`deploy-inc:clearCache deploy-inc:build-inc  deploy-inc:publish-inc -DmustDownloadJars=vdpub-auth,vdpub-common -f pom.xml`

> 该插件会使用指令调用maven编译指定的module，`mvn install -f ${project} -Dmdep.skip=true -DdeployInc.skip=true -Dassembly.skipAssembly=true -Dmaven.test.skip=true` 所以需要配置maven的环境变量