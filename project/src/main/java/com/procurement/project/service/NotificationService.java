package com.procurement.project.service;

import com.procurement.project.dto.NotificationDTO;
import com.procurement.project.model.Notification;
import com.procurement.project.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Send a notification
    public NotificationDTO sendNotification(NotificationDTO notificationDTO){
        Notification notification = Notification.builder()
                .title(notificationDTO.getTitle())
                .message(notificationDTO.getMessage())
                .recipient(notificationDTO.getRecipient())
                .build();
        notificationRepository.save(notification);
        return mapToDTO(notification);

    }

    // Retrieve notifications for a recipient
    public List<NotificationDTO> getNotificationsForRecipient(String recipient){
        List<Notification> notifications = notificationRepository.findByRecipient(recipient);
        return notifications.stream().map(this::mapToDTO).collect(Collectors.toList());

    }
    // Mark a notification as read
    public NotificationDTO markAsRead(Long id){
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return mapToDTO(notification);

    }
     private NotificationDTO mapToDTO(Notification notification){
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getRecipient(),
                notification.getRead(),
                notification.getCreatedAt()

        );
     }
}
