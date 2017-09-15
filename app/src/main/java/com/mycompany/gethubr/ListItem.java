package com.mycompany.gethubr;

public class ListItem {
    private String userImage;
    private String userName;
	private String profileUrl;
    
    public ListItem() {
        super();
    }

    public String getImage() {
        return userImage;
    }

    public void setImage(String avatar_url) {
        this.userImage = avatar_url;
    }

	
    public String getName() {
        return userName;
    }

    public void setName(String login) {
        this.userName = login;
    }
	
	public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String html_url) {
        this.profileUrl = html_url;
    }
	
	}

