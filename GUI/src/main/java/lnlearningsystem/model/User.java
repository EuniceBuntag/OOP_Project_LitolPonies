package lnlearningsystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public String username;
    public String password;
    public Role role;
    public List<UUID> resultIds = new ArrayList<>();

    public User(String u, String p, Role r) { username = u; password = p; role = r; }
}