package rmistore.commons.exceptions;
final public class Rejected extends Exception {
  public Rejected() {
    super();
  }
  public Rejected(String reason) {
    super(reason);
  }
}

