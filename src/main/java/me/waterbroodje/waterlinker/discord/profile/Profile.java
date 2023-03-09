package me.waterbroodje.waterlinker.discord.profile;

import me.waterbroodje.waterlinker.WaterLinker;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.ChatColor;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Profile {
    private final String id;
    private final Long longId;
    private final UUID uuid;
    private final List<Role> roleList;
    private final String nickname;
    private final Color color;
    private final OnlineStatus onlineStatus;
    private final OffsetDateTime creationDate;
    private final OffsetDateTime joinDate;
    private final Member member;

    public Profile(User user, UUID uuid) {
        this.id = user.getId();
        this.longId = user.getIdLong();
        this.uuid = uuid;
        WaterLinker waterLinker = new WaterLinker();
        this.member = Objects.requireNonNull(waterLinker.getGuild().getMember(user));
        this.roleList = member.getRoles();
        this.nickname = member.getNickname();
        this.color = member.getColor();
        this.onlineStatus = this.member.getOnlineStatus();
        this.creationDate = member.getTimeCreated();
        this.joinDate = member.getTimeJoined();
    }

    public String getId() {
        return id;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public OffsetDateTime getJoinDate() {
        return joinDate;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public Long getLongId() {
        return longId;
    }

    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public String getOnlineStatusColored() {
        return switch (onlineStatus) {
            case ONLINE -> ChatColor.GREEN + "Online";
            case IDLE -> ChatColor.GOLD + "Idle";
            case DO_NOT_DISTURB -> ChatColor.RED + "Do Not Disturb";
            case OFFLINE, UNKNOWN, INVISIBLE -> ChatColor.GRAY + "Offline";
        };
    }

    public Color getColor() {
        return color;
    }

    public String getNickname() {
        return nickname;
    }

    public Member getMember() {
        return member;
    }

    public UUID getUuid() {
        return uuid;
    }
}
