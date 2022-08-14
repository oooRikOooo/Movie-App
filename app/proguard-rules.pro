# Add project specific ProGuard rules here.

#-keepnames class androidx.navigation.fragment.NavHostFragment.**{*;}
#-keepnames class * extends androidx.navigation.NavDirections.**{*;}
-keep class * extends android.os.Parcelable.**{*;}
-keep class * extends java.io.Serializable.**{*;}
#-keepnames class * extends androidx.navigation.NavArgs.**{*;}
#-keep class com.example.filmshelper.presentation.screens.FilmDetailsFragmentArgs.**{*;}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.data.ApiService { *; }

# keep everything in this package from being removed or renamed
-keep class com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker.** { *; }

-keep class com.example.filmshelper.presentation.screens.mainFragment.sendFilmWorker.** { *; }

# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.MainActivity { *; }

# keep everything in this package from being removed or renamed
-keep class com.google.android.libraries.places.** { *; }

# keep everything in this package from being removed or renamed
-keep class com.google.android.gms.** { *; }

# keep the class and specified members from being removed or renamed
-keep class android.os.Parcelable { *; }

-keep class java.io.Serializable {*;}

# keep the class and specified members from being removed or renamed
-keep class android.os.Bundle { *; }

# keep everything in this package from being removed or renamed
-keep class com.example.filmshelper.presentation.screens.locationFragments.** { *; }

# keep everything in this package from being removed or renamed
-keep class com.example.filmshelper.data.models.** { *; }

# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.data.models.FirebaseUserFavouriteFilms { *; }

# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.data.models.DisplayableItem { *; }

# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.data.dataSources.mainScreen.RemoteDataSourceImpl_Factory { *; }


# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl_Factory { *; }

# keep the specified class members from being removed or renamed
# only if the class is preserved
-keepclassmembers class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl_Factory { *; }

# keep the class and specified members from being renamed only
-keepnames class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl_Factory { *; }

# keep the specified class members from being renamed only
-keepclassmembernames class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl_Factory { *; }


# keep the class and specified members from being removed or renamed
-keep class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl { *; }

# keep the specified class members from being removed or renamed
# only if the class is preserved
-keepclassmembers class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl { *; }

# keep the class and specified members from being renamed only
-keepnames class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl { *; }

# keep the specified class members from being renamed only
-keepclassmembernames class com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl { *; }

# keep the class and specified members from being removed or renamed
-keep class androidx.navigation.ActivityNavArgsLazyKt { *; }

# keep the specified class members from being removed or renamed
# only if the class is preserved
-keepclassmembers class androidx.navigation.ActivityNavArgsLazyKt { *; }

# keep the class and specified members from being renamed only
-keepnames class androidx.navigation.ActivityNavArgsLazyKt { *; }

# keep the specified class members from being renamed only
-keepclassmembernames class androidx.navigation.ActivityNavArgsLazyKt { *; }

# keep the class and specified members from being removed or renamed
-keep class androidx.navigation.NavArgs { *; }

# keep the specified class members from being removed or renamed
# only if the class is preserved
-keepclassmembers class androidx.navigation.NavArgs { *; }

# keep the class and specified members from being renamed only
-keepnames class androidx.navigation.NavArgs { *; }

# keep the specified class members from being renamed only
-keepclassmembernames class androidx.navigation.NavArgs { *; }

-keep class androidx.navigation.NavDirections { *; }

# keep the specified class members from being removed or renamed
# only if the class is preserved
-keepclassmembers class androidx.navigation.NavDirections { *; }

# keep the class and specified members from being renamed only
-keepnames class androidx.navigation.NavDirections { *; }

# keep the specified class members from being renamed only
-keepclassmembernames class androidx.navigation.NavDirections { *; }
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html


# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
