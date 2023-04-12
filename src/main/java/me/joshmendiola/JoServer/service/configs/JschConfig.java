package me.joshmendiola.JoServer.service.configs;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Setter;

public class JschConfig
{
    @Setter
    private String remoteHost = "localhost";
    @Setter
    private String username = "joshuamendiola";
    @Setter
    private String password = "Turtle75!";

    public ChannelSftp setupJsch() throws JSchException
    {
        JSch jsch = new JSch();
        jsch.setKnownHosts("/Users/joshuamendiola/.ssh/known_hosts");
        Session jschSession = jsch.getSession(username, remoteHost);
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }
}
