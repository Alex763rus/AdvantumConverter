package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;







/**
 * URL
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class URL   {
  @JsonProperty("authority")
  private String authority = null;

  @JsonProperty("content")
  private Object content = null;

  @JsonProperty("defaultPort")
  private Integer defaultPort = null;

  @JsonProperty("file")
  private String file = null;

  @JsonProperty("host")
  private String host = null;

  @JsonProperty("path")
  private String path = null;

  @JsonProperty("port")
  private Integer port = null;

  @JsonProperty("protocol")
  private String protocol = null;

  @JsonProperty("query")
  private String query = null;

  @JsonProperty("ref")
  private String ref = null;

  @JsonProperty("userInfo")
  private String userInfo = null;

  public URL authority(String authority) {
    this.authority = authority;
    return this;
  }

  /**
   * Get authority
   * @return authority
  **/
  


  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public URL content(Object content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
  **/
  


  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public URL defaultPort(Integer defaultPort) {
    this.defaultPort = defaultPort;
    return this;
  }

  /**
   * Get defaultPort
   * @return defaultPort
  **/
  


  public Integer getDefaultPort() {
    return defaultPort;
  }

  public void setDefaultPort(Integer defaultPort) {
    this.defaultPort = defaultPort;
  }

  public URL file(String file) {
    this.file = file;
    return this;
  }

  /**
   * Get file
   * @return file
  **/
  


  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public URL host(String host) {
    this.host = host;
    return this;
  }

  /**
   * Get host
   * @return host
  **/
  


  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public URL path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
  **/
  


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public URL port(Integer port) {
    this.port = port;
    return this;
  }

  /**
   * Get port
   * @return port
  **/
  


  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public URL protocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  /**
   * Get protocol
   * @return protocol
  **/
  


  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public URL query(String query) {
    this.query = query;
    return this;
  }

  /**
   * Get query
   * @return query
  **/
  


  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public URL ref(String ref) {
    this.ref = ref;
    return this;
  }

  /**
   * Get ref
   * @return ref
  **/
  


  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public URL userInfo(String userInfo) {
    this.userInfo = userInfo;
    return this;
  }

  /**
   * Get userInfo
   * @return userInfo
  **/
  


  public String getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(String userInfo) {
    this.userInfo = userInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    URL url = (URL) o;
    return Objects.equals(this.authority, url.authority) &&
        Objects.equals(this.content, url.content) &&
        Objects.equals(this.defaultPort, url.defaultPort) &&
        Objects.equals(this.file, url.file) &&
        Objects.equals(this.host, url.host) &&
        Objects.equals(this.path, url.path) &&
        Objects.equals(this.port, url.port) &&
        Objects.equals(this.protocol, url.protocol) &&
        Objects.equals(this.query, url.query) &&
        Objects.equals(this.ref, url.ref) &&
        Objects.equals(this.userInfo, url.userInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authority, content, defaultPort, file, host, path, port, protocol, query, ref, userInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class URL {\n");
    
    sb.append("    authority: ").append(toIndentedString(authority)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    defaultPort: ").append(toIndentedString(defaultPort)).append("\n");
    sb.append("    file: ").append(toIndentedString(file)).append("\n");
    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
    sb.append("    userInfo: ").append(toIndentedString(userInfo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

