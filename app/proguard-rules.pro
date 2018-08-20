# Add project specific ProGuard rules here.
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

-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-dontnote org.apache.http.params.HttpConnectionParams
-dontnote org.apache.http.params.CoreConnectionPNames
 -dontnote org.apache.http.params.HttpParams
-dontnote org.apache.http.conn.scheme.LayeredSocketFactory
-dontnote org.apache.http.conn.scheme.SocketFactory
-dontnote org.apache.http.conn.scheme.HostNameResolver
-dontnote org.apache.http.conn.ConnectTimeoutException
-dontnote android.net.http.SslCertificate
-dontnote android.net.http.SslCertificate$DName
-dontnote android.net.http.SslError
-dontnote android.net.http.HttpResponseCache

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn  org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn  org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn  org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn  java.beans.Transient
-dontwarn   java.beans.ConstructorProperties
