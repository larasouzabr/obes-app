package com.example.obes.model.User;

import com.example.obes.dao.Request.DonationRequestManager;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;

public abstract class User {
    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getContact();

    public abstract void setContact(String contact);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getAbout();

    public abstract void setAbout(String about);

    public void donationRequest(Request request, ItemRequest item, int idUserMaking, int idUserReceiving) {
        DonationRequestManager.donationRequest(request, item, idUserMaking, idUserReceiving);
    }

    public void cancelDonationRequest(Request request, int idUserMaking, int idUserReceiving) {
        DonationRequestManager.cancelDonationRequest(request, idUserMaking, idUserReceiving);
    }

    public void confirmDonationRequest(Request request) {
        DonationRequestManager.updateStatusRequest(request);
    }
}