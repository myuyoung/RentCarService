package me.changwook.exception.custom;

public class MemberNotFoundException extends RuntimeException {


  public MemberNotFoundException() {
    super();
  }

  public MemberNotFoundException(String message) {
    super(message);
  }

}
