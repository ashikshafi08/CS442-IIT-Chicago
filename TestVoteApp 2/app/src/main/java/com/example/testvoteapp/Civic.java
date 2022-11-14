package com.example.testvoteapp;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Civic implements Serializable {

        private String officeName;
        private String officialName;
        private String address;
        private String party;
        private String phoneNumber;
        private String urlString;
        private String emailId;
        private String photoUrl;
        private String facebookId;
        private String twitterId;
        private String youtubeId;

        public Civic(String officeName, String officialName,
                     String address, String party, String phoneNumber, String urlString,
                     String emailId, String photoUrl, String facebookId, String twitterId,
                     String youtubeId) {
                this.officeName = officeName;
                this.officialName = officialName;
                this.address = address;
                this.party = party;
                this.phoneNumber = phoneNumber;
                this.urlString = urlString;
                this.emailId = emailId;
                this.photoUrl = photoUrl;
                this.facebookId = facebookId;
                this.twitterId = twitterId;
                this.youtubeId = youtubeId;
        }

        public Civic() {

        }

        public String getOfficeName() {
                return officeName;
        }

        public String getOfficialName() {
                return officialName;
        }

        public String getAddress() {
                return address;
        }

        public String getParty() {
                return party;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }

        public String getUrlString() {
                return urlString;
        }

        public String getEmailId() {
                return emailId;
        }

        public String getPhotoUrl() {
                return photoUrl;
        }

        public String getFacebookId() {
                return facebookId;
        }

        public String getTwitterId() {
                return twitterId;
        }

        public String getYoutubeId() {
                return youtubeId;
        }

        public void setOfficeName(String officeName) {
                this.officeName = officeName;
        }

        public void setOfficialName(String officialName) {
                this.officialName = officialName;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public void setParty(String party) {
                this.party = party;
        }

        public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
        }

        public void setUrlString(String urlString) {
                this.urlString = urlString;
        }

        public void setEmailId(String emailId) {
                this.emailId = emailId;
        }

        public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
        }

        public void setFacebookId(String facebookId) {
                this.facebookId = facebookId;
        }

        public void setTwitterId(String twitterId) {
                this.twitterId = twitterId;
        }

        public void setYoutubeId(String youtubeId) {
                this.youtubeId = youtubeId;
        }
}
