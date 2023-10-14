package core.utility;

import core.database.getTable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class insertUser {

    // THESE TWO WAYS ON INSERTING USERS INTO THE DATABASE
    // ENSURE THAT NO USER IS EVER SKIPPED DUE TO NOT BEING CACHED
    // BOTH HAVE TO BE RUN IN UNISON
    public static void onReady(GuildReadyEvent EVENT){

        // Create a connection to the database.
        Connection con = getTable.openConnection();

        // Get all CACHED Users and insert them into the database (if able)
        // When the user already exists in the database they are skipped.
        List<Member> members = EVENT.getGuild().getMembers();
        for (Member member : members){
            // Convert MEMBER class to a string user.
            String user = member.toString();
            // Trim the string to only extract the user_id.
            int index = user.indexOf("id=");
            String user_id = user.substring(index+3, index + 21);

            // Try to insert extracted user_id.
            try {
                getTable.insert(con, user_id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Close the connection after each user was looped over.
        getTable.closeConnection(con);
    }

    public static void onMemberJoin(GuildMemberJoinEvent EVENT){

        // Create a connection to the database.
        Connection con = getTable.openConnection();

        // When a member joins the discord server take their details and extract user_id.
        String user = String.valueOf(EVENT.getMember());
        int index = user.indexOf("id=");
        String user_id = user.substring(index+3, index + 21);
        // Try to insert into the database if they aren't already in it.
        try {
            getTable.insert(con, user_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Close the connection after the user was added.
        getTable.closeConnection(con);
    }

}
