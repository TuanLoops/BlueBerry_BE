package com.blueberry.repository;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByReceiverOrderByTimeStampDesc(AppUser receiver);
}
