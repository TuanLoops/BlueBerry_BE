package com.blueberry.model.request;

import com.blueberry.model.app.FriendRequestStatus;
import lombok.Data;

@Data
public class FriendRequestResponse {
    Long requestId;
    FriendRequestStatus status;
}
