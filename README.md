# APermission

##在工程的build.gradle中添加如下代码

      classpath 'org.aspectj:aspectjtools:1.8.9'
      classpath 'org.aspectj:aspectjweaver:1.8.9'

## 在app moudle中的build.gradle添加如下代码


      import org.aspectj.bridge.IMessage
      import org.aspectj.bridge.MessageHandler
      import org.aspectj.tools.ajc.Main


      buildscript { //在编译时使用Aspect 专门的编译器,不在使用传统的javac来编译
          repositories {
              mavenCentral()
          }
          dependencies {
              //AS3.0.1 gradle4.4 + r17的ndk环境
              //AS3.2.1 gradle4.6
              //
              classpath 'org.aspectj:aspectjtools:1.8.9'
              classpath 'org.aspectj:aspectjweaver:1.8.9'
          }
      }
      final def log = project.logger
      final def variants = project.android.applicationVariants
      variants.all { variant ->
          if (!variant.buildType.isDebuggable()) {
              log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
              return;
          }

          JavaCompile javaCompile = variant.javaCompile
          javaCompile.doLast {
              String[] args = ["-showWeaveInfo",
                               "-1.8",
                               "-inpath", javaCompile.destinationDir.toString(),
                               "-aspectpath", javaCompile.classpath.asPath,
                               "-d", javaCompile.destinationDir.toString(),
                               "-classpath", javaCompile.classpath.asPath,
                               "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
              log.debug "ajc args: " + Arrays.toString(args)

              MessageHandler handler = new MessageHandler(true);
              new Main().run(args, handler);
              for (IMessage message : handler.getMessages(null, true)) {
                  switch (message.getKind()) {
                      case IMessage.ABORT:
                      case IMessage.ERROR:
                      case IMessage.FAIL:
                          log.error message.message, message.thrown
                          break;
                      case IMessage.WARNING:
                          log.warn message.message, message.thrown
                          break;
                      case IMessage.INFO:
                          log.info message.message, message.thrown
                          break;
                      case IMessage.DEBUG:
                          log.debug message.message, message.thrown
                          break;
                  }
              }
          }
      }

##使用,具体使用请参考app中的MainActivity


  ###请求多个权限
  
    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    public void requestMorePermissions(){
        Toast.makeText(this,"请求权限成功",Toast.LENGTH_SHORT).show();
    }

    @PermissionCancled(requestCode = 1000)
    public void requestMorePermissionCancle(){
        Toast.makeText(this,"权限取消",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 1001)
    public void requestMorePermissionDenied(){
        Toast.makeText(this,"权限拒绝",Toast.LENGTH_SHORT).show();
    }
    
 ###请求单个权限
 
    @Permission(Manifest.permission.READ_PHONE_STATE)
    public void requestOnePermission(){
        Toast.makeText(this,"请求权限成功",Toast.LENGTH_SHORT).show();
    }

    @PermissionCancled
    public void requestOnePermissionCancle(){
        Toast.makeText(this,"权限取消",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void requestOnePermissionDenied(){
        Toast.makeText(this,"权限拒绝",Toast.LENGTH_SHORT).show();
    }
 

#注意编译工具

       AS3.0.1 gradle4.4 + r17的ndk环境
       
       AS3.2.1 gradle4.6
  
