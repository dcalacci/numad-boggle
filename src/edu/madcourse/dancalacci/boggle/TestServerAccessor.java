package edu.madcourse.dancalacci.boggle;
import java.util.ArrayList;

public class TestServerAccessor {
  private final ServerAccessor sa;
  private final ArrayList<String> emptyArrayList = new ArrayList<String>();

  public TestServerAccessor() {
    emptyArrayList.add("");
    this.sa = new ServerAccessor();
    sa.clear();
  }
  /*
  public static void main(String[] args) {
    TestServerAccessor test = new TestServerAccessor();
    test.requests();
    test.received();
    test.login();
    test.games();

    test.summarize();

  }

  private void summarize() {
    System.out.println();
    System.out.println (totalErrors + " errors found in " + totalTests +
        " tests.");
  }

  private void requests() {
    final ArrayList<String> userlist1 = new ArrayList<String>();
    userlist1.add("user1");
    userlist1.add("user2");
    userlist1.add("user3");
    userlist1.add("user4");

    try {
      assertTrue("emptyReq", sa.getSentRequests("user1").equals(emptyArrayList));
      sa.addRequest("user1", "user2");
      assertTrue("getReqsUser1", sa.getSentRequests("user1").contains("user2"));
      assertTrue("arrayListToString",
          sa.arrayListToString(sa.getSentRequests("user1")).equals("user2,"));
      sa.addRequest("user1", "user3");
      assertTrue("getReqsUser1", sa.getSentRequests("user1").contains("user2")
          && sa.getSentRequests("user1").contains("user3"));
      assertTrue("arrayListToString2",
          sa.arrayListToString(sa.getSentRequests("user1")).equals("user2,user3,"));
      ArrayList<String> toAdd = new ArrayList<String>();
      toAdd.add("user4");
      toAdd.add("user5");
      sa.addRequestList("user1", toAdd);
      // testing adding and getting multiple requests
      assertTrue("arrayListAddMultiple",
          sa.arrayListToString(sa.getSentRequests("user1")).equals(
              "user2,user3,user4,user5,"));
      
      sa.removeRequest("user1", "user2");
      assertFalse("arrayListRemove1",
          sa.getSentRequests("user1").contains("user2"));
      sa.removeRequest("user1", "user3");
      assertFalse("arrayListRemove2",
          sa.getSentRequests("user1").contains("user3"));

    }
    catch(Exception e) {
      System.out.println("Exception thrown during requests tests:");
      System.out.println(e);
      sa.clear(); //clear the keys and values
      assertTrue("setters", false);
    }
  }
  
  private void received() {
    final ArrayList<String> userlist2 = new ArrayList<String>();
    userlist2.add("user2");
    userlist2.add("user3");
    userlist2.add("user4");

    try {
      sa.addReceived("user1", "user2");
      assertTrue("addReceived1",
          sa.getReceivedRequests("user1").contains("user2"));
      
      sa.addReceived("user1", "user3");
      assertTrue("addReceived2",
          sa.getReceivedRequests("user1").contains("user2") &&
          sa.getReceivedRequests("user1").contains("user3"));
      sa.clear();
      
      sa.addReceivedList("user1", userlist2);
      ArrayList<String> user1Recs = sa.getReceivedRequests("user1");
      int count = 0;
      for (String user : userlist2) {
        assertTrue("addAllReceived1" + count,
            user1Recs.contains(user));
        count++;
      }
      sa.removeReceived("user1", "user2");
      assertFalse("removeReceived1",
          sa.getReceivedRequests("user1").contains("user2"));
      //ArrayList<String> curRecs

    }
    catch(Exception e) {
      System.out.println("Exception thrown during received tests:");
      System.out.println(e);
      assertTrue("setters", false);
    }
  }

  private void login() {
    try {
      String user1 = "user1";
      String pass1 = "suspension1";
      String user2 = "user2";
      String pass2 = "suspension2";
      String user3 = "user3";
      String pass3 = "suspension,3";

      sa.clear();
      assertTrue("canRegister1",
          sa.canRegister(user1, pass1));
      sa.register(user1, pass1);
      assertTrue("canRegister2",
          sa.canRegister(user2, pass2));
      sa.register(user2, pass2);
      assertFalse("canRegister3",
          sa.canRegister(user1, pass2));
      assertFalse("canRegister4",
          sa.canRegister(user3, pass3));
      assertTrue("alreadyRegistered1",
          sa.alreadyRegistered(user1));
      assertFalse("alreadyRegistered2",
          sa.alreadyRegistered(user3));
      sa.clear();
    } catch(Exception e) {
      System.out.println("Exception thrown during login tests:");
      System.out.println(e);
      assertTrue("login", false);
    }
  }

  private void games() {
    try {
      String user1 = "user1";
      String user2 = "user2";
      String user3 = "user3";
      String user4 = "user4";

      sa.clear();
      sa.register(user1, "pass");
      sa.register(user2, "pass");
      sa.register(user3, "pass");
      sa.register(user4, "pass");

      sa.addGame(user1, user2);
      assertTrue("doesGameExist1",
          sa.doesGameExist(user1, user2));
      assertFalse("doesGameExist2",
          sa.doesGameExist(user1, user3));
      assertFalse("doesGameExist3",
          sa.doesGameExist(user2, user3));
      assertTrue("doesGameExist4",
          sa.doesGameExist(user2, user1));
      sa.addGame(user1, user3);
      assertTrue("doesGameExist5",
          sa.doesGameExist(user1, user3));
      assertTrue("getGames1",
          sa.getGames(user1).contains(user3) &&
          sa.getGames(user1).contains(user2));
      assertFalse("getGames2",
          sa.getGames(user1).contains(user4));
    } catch(Exception e) {
      System.out.println("Exception thrown during games tests:");
      System.out.println(e);
      assertTrue("games", false);
    }
  }


  //////////////////////////////////////////////////////
  private int totalTests = 0;       // tests run so far
  private int totalErrors = 0;      // errors so far

  private void assertTrue (boolean result) {
    assertTrue ("anonymous", result);
  }

  // Prints failure report if the result is not true.

  private void assertTrue (String name, boolean result) {
    if (! result) {
      System.out.println ();
      System.out.println ("***** Test failed ***** "
          + name + ": " +totalTests);
      totalErrors = totalErrors + 1;
    }
    totalTests = totalTests + 1;
  }

  // For anonymous tests.  Deprecated.

  private void assertFalse (boolean result) {
    assertTrue (! result);
  }

  // Prints failure report if the result is not false.

  private void assertFalse (String name, boolean result) {
    assertTrue (name, ! result);
  }
  */
}
