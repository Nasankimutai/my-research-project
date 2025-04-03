package com.procurement.project.controller;

import com.procurement.project.dto.NotificationDTO;
import com.procurement.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Endpoint to send a notification
    @PostMapping("/send")
    public ResponseEntity<NotificationDTO> sendNotification(@RequestBody NotificationDTO notificationDTO){
        NotificationDTO responseDTO = notificationService.sendNotification(notificationDTO);
        return ResponseEntity.ok(responseDTO);
    }

    //Endpoint to retrieve notifications for a recipient
    @GetMapping("/{recipient}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable String recipient){
        List<NotificationDTO> notifications = notificationService.getNotificationsForRecipient(recipient);
        return ResponseEntity.ok(notifications);
    }

    // Endpoint to mark a notification as read
    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable Long id){
        NotificationDTO notificationDTO = notificationService.markAsRead(id);
        return ResponseEntity.ok(notificationDTO);
    }
}
