1.错误一   cannot find implementation for XXX. XXX_Impl does not exist
 
 按照官方demo进行调试，正常添加依赖，并正常的声明@Dao @entity ，@database。正常初始化后提示。
 
 原因官方demo是使用gradle构建文件的注解使用java类型。而自己所见项目是kotlin方式。需要添加新的kotlin apt 注解


app module 中的构建文件

plugins {
id 'com.android.application'
id 'org.jetbrains.kotlin.android'
id 'kotlin-kapt'  //这里需要手动添加
}


android {
compileSdk 31

    defaultConfig {
        applicationId "com.kuaidao.jetpackroom"
        minSdk 25
        targetSdk 31
        versionCode 1
        versionName "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = ["room.schemaLocation":
                                     "$projectDir/schemas".toString()]
            }
        }

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }

    //开启ViewBind
    viewBinding {
        enabled = true
    }

}

dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    //room

    def room_version = "2.4.2"

     implementation "androidx.room:room-runtime:$room_version"
     kapt "androidx.room:room-compiler:$room_version"   

    // optional - RxJava2 support for Room
    implementation "androidx.room:room-rxjava2:$room_version"

    // optional - RxJava3 support for Room
    implementation "androidx.room:room-rxjava3:$room_version"

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation "androidx.room:room-guava:$room_version"

    // optional - Test helpers
    testImplementation "androidx.room:room-testing:$room_version"

    // optional - Paging 3 Integration
    implementation "androidx.room:room-paging:2.5.0-alpha01"

}

错误2

不能在主线程进行数据库操作。room默认设置必须在子线程中操作。如果想在主线程操作需要在构建数据库的时候，调用
.allowMainThreadQueries()

    Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
    .addCallback(new Callback() {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
    super.onCreate(db);
    executors.diskIO().execute(() -> {
    // Generate the data for pre-population
    AppDatabase database = AppDatabase.getInstance(appContext, executors);
    List<User> products = DataGenerator.generateProducts();
    insertData(database, products);
    // notify that the database was created and it's ready to be used
    database.setDatabaseCreated();
    });
    }
    }).allowMainThreadQueries()
    .build();


3 .autoMigration Failure in 'MyAutoMigration': The table renamed from 'User' to 'User1' is not found in the new version of the database.



