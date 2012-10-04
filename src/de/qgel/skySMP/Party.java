package de.qgel.skySMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Party
  implements Serializable
{
  private static final long serialVersionUID = 7L;
  private String pLeader;
  private String m2;
  private String m3;
  private String m4;
  private Island pIsland;
  private int pSize;
  private static int maxSize = 4;
  private List<String> members;

  public Party(String leader, String member2, Island island)
  {
    this.pLeader = leader;
    this.m2 = member2;
    this.m3 = "EmptySlot";
    this.m4 = "EmptySlot";
    this.pSize = 2;
    this.pIsland = island;
    this.members = new ArrayList();
  }

  public String getLeader()
  {
    return this.pLeader;
  }

  public Island getIsland() {
    return this.pIsland;
  }

  public int getSize() {
    return this.pSize;
  }

  public boolean hasMember(String player)
  {
    return (player.equalsIgnoreCase(this.pLeader)) || (player.equalsIgnoreCase(this.m2)) || (player.equalsIgnoreCase(this.m3)) || (player.equalsIgnoreCase(this.m4));
  }

  public List<String> getMembers()
  {
    this.members.clear();
    this.members.add(this.m2);
    this.members.add(this.m3);
    this.members.add(this.m4);
    return this.members;
  }

  public boolean changeLeader(String oLeader, String nLeader)
  {
    if (oLeader.equalsIgnoreCase(this.pLeader))
    {
      if (nLeader.equalsIgnoreCase(this.m2))
      {
        this.m2 = oLeader;
        this.pLeader = nLeader;
        return true;
      }if (nLeader.equalsIgnoreCase(this.m3))
      {
        this.m3 = oLeader;
        this.pLeader = nLeader;
        return true;
      }if (nLeader.equalsIgnoreCase(this.m4))
      {
        this.m4 = oLeader;
        this.pLeader = nLeader;
        return true;
      }
      return false;
    }
    return false;
  }

  public int getMax()
  {
    return maxSize;
  }

  public boolean addMember(String nMember)
  {
    if (getSize() < maxSize)
    {
      if (this.m2.equals("EmptySlot"))
      {
        this.m2 = nMember;
        this.pSize += 1;
      } else if (this.m3.equals("EmptySlot"))
      {
        this.m3 = nMember;
        this.pSize += 1;
      } else if (this.m4.equals("EmptySlot"))
      {
        this.m4 = nMember;
        this.pSize += 1;
      }
      return true;
    }

    return false;
  }

  public int removeMember(String oMember)
  {
    if (oMember.equalsIgnoreCase(this.pLeader))
    {
      return 0;
    }if (oMember.equalsIgnoreCase(this.m2))
    {
      this.m2 = this.m3;
      this.m3 = this.m4;
      this.m4 = "EmptySlot";
      this.pSize -= 1;
      return 2;
    }if (oMember.equalsIgnoreCase(this.m3))
    {
      this.m3 = this.m4;
      this.m4 = "EmptySlot";
      this.pSize -= 1;
      return 2;
    }if (oMember.equalsIgnoreCase(this.m4))
    {
      this.m4 = "EmptySlot";
      this.pSize -= 1;
      return 2;
    }
    return 1;
  }
}