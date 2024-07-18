package com.example.MuseumTicketing.appGuide.audio;

import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeDTO;
import com.example.MuseumTicketing.Guide.mpType.FileType;
import com.example.MuseumTicketing.Guide.mpType.FileTypeRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/mediaTypeData")
public class AudioController {
    @Autowired
    private AudioService audioService;
    @Autowired
    private FileTypeRepo fileTypeRepo;
    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;
    @Autowired
    private SubComIdRepo subComIdRepo;
    @Autowired
    private MainTopicEngRepo mainTopicEngRepo;
    @Autowired
    private MainTopicMalRepo mainTopicMalRepo;
    @Autowired
    private FirstTopicEngRepo firstTopicEngRepo;
    @Autowired
    private FirstTopicMalRepo firstTopicMalRepo;

    @PostMapping(path = "/uploadMediaTypeData")
    public ResponseEntity<?>uploadAudioFile(@RequestParam MultipartFile file,
                                            @RequestParam String uId,
                                            @RequestParam Integer mtId){
        try {
            if (uId == null || mtId == null ||uId.isEmpty()||"undefined".equalsIgnoreCase(uId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional =fileTypeRepo .findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
                    Optional<MainTopicEng> mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(uId);
                    Optional<MainTopicMal> mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(uId);
                    Optional<FirstTopicEng> firstTopicEngOptional = firstTopicEngRepo.findByfsUid(uId);
                    Optional<FirstTopicMal> firstTopicMalOptional = firstTopicMalRepo.findByFsUid(uId);

                    if (mainTopicEngOptional.isPresent()){
                        MainTopicEng mainTopicEng = mainTopicEngOptional.get();
                        if (mainTopicEng.getMEngUid().equals(uId)){
                            MediaTypeDTO mediaTypeDTO = audioService.uploadAudioMain(uId,file);
                            return new ResponseEntity<>(mediaTypeDTO,HttpStatus.OK);
                        }
                    } else if (mainTopicMalOptional.isPresent()) {
                        MainTopicMal mainTopicMal = mainTopicMalOptional.get();
                        if (mainTopicMal.getMMalUid().equals(uId)){
                            MediaTypeDTO mediaTypeDTO = audioService.uploadAudioMain(uId,file);
                            return new ResponseEntity<>(mediaTypeDTO,HttpStatus.OK);
                        }
                    } else if (firstTopicEngOptional.isPresent()) {
                        FirstTopicEng firstTopicEng = firstTopicEngOptional.get();
                        if (firstTopicEng.getFsUid().equals(uId)){
                            MediaTypeDTO mediaTypeDTO = audioService.uploadAudioFirst(file,uId);
                            return new ResponseEntity<>(mediaTypeDTO,HttpStatus.OK);
                        }
                    } else if (firstTopicMalOptional.isPresent()) {
                        FirstTopicMal firstTopicMal = firstTopicMalOptional.get();
                        if (firstTopicMal.getFsUid().equals(uId)){
                            MediaTypeDTO mediaTypeDTO = audioService.uploadAudioFirst(file,uId);
                            return new ResponseEntity<>(mediaTypeDTO,HttpStatus.OK);
                        }
                    }
                } else if (fData != null && "Video".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId> commonIdQRCodeOptional = commonQRParaIdRepo.findByCommonId(uId);
                    Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(uId);
                    if (commonIdQRCodeOptional.isPresent()){
                        CommonQRParaId commonIdQRCode = commonIdQRCodeOptional.get();
                        if (commonIdQRCode.getCommonId().equals(uId)){
                            String engId = commonIdQRCode.getEngId();
                            String malId = commonIdQRCode.getMalId();
                            MediaTypeDTO mediaTypeDTO = audioService.uploadVideoMain(uId,file,engId,malId);
                            return new ResponseEntity<>(mediaTypeDTO,HttpStatus.OK);
                        }
                    }else if (subComIdOptional.isPresent()){
                        SubComId commonIdFs = subComIdOptional.get();
                        if (commonIdFs.getFsCommonId().equals(uId)){
                            String engId = commonIdFs.getFsEngId();
                            String malId = commonIdFs.getFsMalId();
//                            MediaTypeDTO mediaTypeDTO = audioService.uploadVideoFirst(uId,file,engId,malId);
//                            return new ResponseEntity<>(mediaTypeDTO,HttpStatus.OK);
                            return audioService.uploadVideoFirst(uId,file,engId,malId);
                        }
                    }else {
                        return new ResponseEntity<>("CommonId is not generated. Please generate it.",HttpStatus.NO_CONTENT);
                    }
                } else {
                    return new ResponseEntity<>("FileType is not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File is not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
