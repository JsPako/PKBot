package core.utility;

import core.database.getTable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class insertUser {

    public static void onReady(GuildReadyEvent EVENT){

        Connection con = getTable.openConnection();

        List<Member> members = EVENT.getGuild().getMembers();
        for (Member member : members){
            String user = member.toString();
            int index = user.indexOf("id=");
            String user_id = user.substring(index+3, index + 21);
            try {
                getTable.insert(con, user_id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        getTable.closeConnection(con);

    }

    public static void onMemberJoin(){}

}
