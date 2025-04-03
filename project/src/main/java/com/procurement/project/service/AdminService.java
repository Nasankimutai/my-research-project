package com.procurement.project.service;

import com.procurement.project.dto.SystemSettingDTO;
import com.procurement.project.model.SystemSetting;
import com.procurement.project.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    // Retrieve system settings (assuming a single settings record)
    public SystemSettingDTO getSystemSettings() {
        Optional<SystemSetting> optionalSetting = systemSettingRepository.findAll().stream().findFirst();
        SystemSetting setting = optionalSetting.orElse(SystemSetting.builder().anomalyThreshold(0.7).build());
        return mapToDTO(setting);
    }

    // Update system settings
    public SystemSettingDTO updateSystemSettings(SystemSettingDTO settingDTO) {
        Optional<SystemSetting> optionalSetting = systemSettingRepository.findAll().stream().findFirst();
        SystemSetting setting = optionalSetting.orElse(new SystemSetting());
        setting.setAnomalyThreshold(settingDTO.getAnomalyThreshold());
        setting = systemSettingRepository.save(setting);
        return mapToDTO(setting);
    }

    // Health-check: Check if the backend (and optionally the ML service) is operational.
    public String healthCheck() {
        // A basic implementation; in a real system, you might ping the ML service endpoint.
        return "Backend is healthy. ML service is operational.";
    }

    private SystemSettingDTO mapToDTO(SystemSetting setting) {
        return new SystemSettingDTO(setting.getAnomalyThreshold());
    }
}
