package com.example.MuseumTicketing.backupDetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BackupService {
    @Value("${app.backUpData.source_dir}")
    private String source_dir;
    @Value("${app.backUpData.backup_dir}")
    private String backup_dir;

    @Scheduled(cron = "0 0 2 * * SUN")
    public void performBackup(){
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            Path backupFile = Paths.get(backup_dir,"backup_"+timeStamp + " .tar.gz");
            ProcessBuilder pb = new ProcessBuilder("tar","-czf",backupFile.toString(),"-C",source_dir,".");
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
