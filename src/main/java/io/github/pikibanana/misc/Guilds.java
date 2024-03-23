package io.github.pikibanana.misc;

import java.util.List;

public enum Guilds {


    ASTRAL("Astral",List.of(
            "PikiBanana",
            "A1eq",
            "Sllmey_sllme",
            "Eyalc",
            "Ifluxx",
            "Ludde070",
            "teuli",
            "Vyian",
            "Erisab",
            "slackwalker",
            "SheeplLord29",
            "Lycheesis",
            "Draconix95",
            ".permabann",
            "Giga_Whale"

    ))
    ;

    private String guildName;
    private List<String> membersNames;

    Guilds(String guildName, List<String> membersNames){
        this.guildName = guildName;
        this.membersNames = membersNames;
    }


    public List<String> getMembersNames() {
        return membersNames;
    }

    public String getGuildName() {
        return guildName;
    }
}
