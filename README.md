# informix-jdbc
Connection database jdbc Java Informix

# Dependency
com.ibm.informix 4.50


# Class
ConnectionIfx(HashMap<String, String> Parameters)
  String Host = Parameters.get("host");
  String Base = Parameters.get("base");
  String Inst = Parameters.get("inst"); //Instancie
  String User = Parameters.get("user");
  String Pass = Parameters.get("pass");

# Example
App.java
