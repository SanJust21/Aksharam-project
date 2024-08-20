package com.example.MuseumTicketing.appGuide.audio;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.Pdf.PdfData;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirst;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMain;
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
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/mediaTypeData")
@CrossOrigin
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
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private ErrorService errorService;

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
                } else if (fData!=null&&"PDF".equalsIgnoreCase(fData)) {
                    Optional<MainTopicEng> mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(uId);
                    Optional<MainTopicMal> mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(uId);
                    if (mainTopicMalOptional.isPresent()){
                        MainTopicMal mainTopicMal = mainTopicMalOptional.get();
                        if (mainTopicMal.getMMalUid().equals(uId)){
                            return audioService.uploadPDF(uId,file);
                        }
                    } else if (mainTopicEngOptional.isPresent()) {
                        MainTopicEng mainTopicEng = mainTopicEngOptional.get();
                        if (mainTopicEng.getMEngUid().equals(uId)){
                            return audioService.uploadPDF(uId,file);
                        }
                    }
                } else {
                    return new ResponseEntity<>("FileType is not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File is not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "updateMediaPlayer/{commonId}")
    public ResponseEntity<?>updateMediaPlayerData(@PathVariable String commonId,@RequestParam Integer mtId,
                                            @RequestParam MultipartFile[] file,@RequestParam Integer dType,
                                                  @RequestParam List<Integer> ids) {
        try {
            if (commonId == null || ids ==null ||mtId == null || commonId.isEmpty() || "undefined".equalsIgnoreCase(commonId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional = fileTypeRepo.findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
                    Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
                    if (commonQRParaIdOptional.isPresent()) {
                        CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                        String engId = commonQRParaId.getEngId();
                        String malId = commonQRParaId.getMalId();
                        Optional<DataType> dataType = dataTypeRepo.findById(dType);
                        if (dataType.isPresent()) {
                            DataType dataType1 = dataType.get();
                            String data = dataType1.getTalk();
                            if ("Malayalam".equalsIgnoreCase(data)) {
                                List<AudioMain> response = new ArrayList<>();
                                for (int i =0; i < file.length;i++){
                                    response.add(audioService.updateAudioMain(malId,commonId,file[i],ids.get(i)));
                                }
                                return new ResponseEntity<>(response,HttpStatus.OK);
                            } else if ("English".equalsIgnoreCase(data)) {
                                List<AudioMain> response = new ArrayList<>();
                                for (int i =0; i < file.length;i++){
                                    response.add(audioService.updateAudioMain(engId,commonId,file[i],ids.get(i)));
                                }
                                return new ResponseEntity<>(response,HttpStatus.OK);
                            } else {
                                return new ResponseEntity<>("Data Type is required", HttpStatus.BAD_REQUEST);
                            }
                        }
                    } else if (subComIdOptional.isPresent()) {
                        SubComId subComId = subComIdOptional.get();
                        String engId = subComId.getFsEngId();
                        String malId = subComId.getFsMalId();
                        Optional<DataType> dataType = dataTypeRepo.findById(dType);
                        if (dataType.isPresent()) {
                            DataType dataType1 = dataType.get();
                            String data = dataType1.getTalk();
                            if ("Malayalam".equalsIgnoreCase(data)) {
                                List<AudioFirst> response = new ArrayList<>();
                                for (int i =0; i < file.length;i++){
                                    response.add(audioService.updateAudioFirst(malId,commonId,file[i],ids.get(i)));
                                }
                                return new ResponseEntity<>(response,HttpStatus.OK);
                                //return audioService.updateAudioFirst(malId, commonId, file);
                            } else if ("English".equalsIgnoreCase(data)) {
                                List<AudioFirst> response = new ArrayList<>();
                                for (int i =0; i < file.length;i++){
                                    response.add(audioService.updateAudioFirst(malId,commonId,file[i],ids.get(i)));
                                }
                                return new ResponseEntity<>(response,HttpStatus.OK);
                                //return audioService.updateAudioFirst(engId, commonId, file);
                            } else {
                                return new ResponseEntity<>("Data Type is required", HttpStatus.BAD_REQUEST);
                            }
                        }
                    } else {
                        return new ResponseEntity<>("CommonId is not valid. Please check", HttpStatus.BAD_REQUEST);
                    }
                } else if (fData!=null && "PDF".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
                    if (commonQRParaIdOptional.isPresent()){
                        CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                        String engId = commonQRParaId.getEngId();
                        String malId =commonQRParaId.getMalId();
                        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dType);
                        if (dataTypeOptional.isPresent()){
                            DataType dataType = dataTypeOptional.get();
                            String data = dataType.getTalk();
                            if ("Malayalam".equalsIgnoreCase(data)){
                                List<PdfData> response = new ArrayList<>();
                                for (int i =0; i < file.length;i++){
                                    response.add(audioService.updatePDF(malId,commonId,file[i],ids.get(i)));
                                }
                                return new ResponseEntity<>(response,HttpStatus.OK);
                                //return audioService.updatePDF(file,malId);
                            } else if ("English".equalsIgnoreCase(data)) {
                                List<PdfData> response = new ArrayList<>();
                                for (int i =0; i < file.length;i++){
                                    response.add(audioService.updatePDF(engId,commonId,file[i],ids.get(i)));
                                }
                                return new ResponseEntity<>(response,HttpStatus.OK);
                                //return audioService.updatePDF(file,engId);
                            }return new ResponseEntity<>("Language is not mentioned",HttpStatus.NOT_FOUND);
                        }return new ResponseEntity<>("Language is required",HttpStatus.BAD_REQUEST);
                    }return new ResponseEntity<>("CommonId : "+commonId+" is required. Enter valid commonId",HttpStatus.BAD_REQUEST);
                } else if (fData != null && "Video".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
                    Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
                    if (commonQRParaIdOptional.isPresent()) {
//
                        List<VideoMain> response = new ArrayList<>();
                        for (int i =0; i < file.length;i++){
                            response.add(audioService.updateVideoMain(commonId,file[i],ids.get(i)));
                        }
                        return new ResponseEntity<>(response,HttpStatus.OK);

                    } else if (subComIdOptional.isPresent()) {
                        SubComId subComId = subComIdOptional.get();
                        List<VideoFirst> response = new ArrayList<>();
                        for (int i =0; i < file.length;i++){
                            response.add(audioService.updateVideoFirst(commonId,file[i],ids.get(i)));
                        }
                        return new ResponseEntity<>(response,HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("File is not present. Resend the file.", HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }catch(Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteMediaPlayer/{commonId}")
    public ResponseEntity<?>deleteMediaPlayer(@PathVariable String commonId,@RequestParam Integer mtId,
                                              @RequestParam Integer dType){
        try {
            if (commonId == null || mtId == null || commonId.isEmpty() || "undefined".equalsIgnoreCase(commonId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional = fileTypeRepo.findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
                    Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
                    if (commonQRParaIdOptional.isPresent()) {
                        CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                        String engId = commonQRParaId.getEngId();
                        String malId = commonQRParaId.getMalId();
                        Optional<DataType> dataType = dataTypeRepo.findById(dType);
                        if (dataType.isPresent()) {
                            DataType dataType1 = dataType.get();
                            String data = dataType1.getTalk();
                            if ("Malayalam".equalsIgnoreCase(data)) {
                                int count = audioService.deleteAudioMain(malId, commonId);
                                if (count>0){
                                    return new ResponseEntity<>("Audio is deleted",HttpStatus.OK);
                                }else {
                                    return new ResponseEntity<>("Failed to delete audio.Try again",HttpStatus.BAD_REQUEST);
                                }
                            } else if ("English".equalsIgnoreCase(data)) {
                                int count =  audioService.deleteAudioMain(engId, commonId);
                                if (count>0){
                                    return new ResponseEntity<>("Audio is deleted",HttpStatus.OK);
                                }else {
                                    return new ResponseEntity<>("Failed to delete audio.Try again",HttpStatus.BAD_REQUEST);
                                }
                            } else {
                                return new ResponseEntity<>("Data Type is required", HttpStatus.BAD_REQUEST);
                            }
                        }
                    } else if (subComIdOptional.isPresent()) {
                        SubComId subComId = subComIdOptional.get();
                        String engId = subComId.getFsEngId();
                        String malId = subComId.getFsMalId();
                        Optional<DataType> dataType = dataTypeRepo.findById(dType);
                        if (dataType.isPresent()) {
                            DataType dataType1 = dataType.get();
                            String data = dataType1.getTalk();
                            if ("Malayalam".equalsIgnoreCase(data)) {
                                int count = audioService.deleteAudioFirst(malId, commonId);
                                if (count>0){
                                    return new ResponseEntity<>("Video is deleted",HttpStatus.OK);
                                }else {
                                    return new ResponseEntity<>("Failed to delete video.Try again",HttpStatus.BAD_REQUEST);
                                }
                            } else if ("English".equalsIgnoreCase(data)) {
                                int count = audioService.deleteAudioFirst(engId, commonId);
                                if (count>0){
                                    return new ResponseEntity<>("Video is deleted",HttpStatus.OK);
                                }else {
                                    return new ResponseEntity<>("Failed to delete video.Try again",HttpStatus.BAD_REQUEST);
                                }
                            } else {
                                return new ResponseEntity<>("Data Type is required", HttpStatus.BAD_REQUEST);
                            }
                        }
                    } else {
                        return new ResponseEntity<>("CommonId is not valid. Please check", HttpStatus.BAD_REQUEST);
                    }
                } else if (fData!=null && "PDF".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByCommonId(commonId);
                    if (commonQRParaIdOptional.isPresent()){
                        CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                        String engId=commonQRParaId.getEngId();;
                        String malId=commonQRParaId.getMalId();
                        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dType);
                        if (dataTypeOptional.isPresent()){
                            DataType dataType = dataTypeOptional.get();
                            String data = dataType.getTalk();
                            if ("Malayalam".equalsIgnoreCase(data)){
                                return audioService.deletePdfMain(malId);
                            } else if ("English".equalsIgnoreCase(data)) {
                                return audioService.deletePdfMain(engId);
                            }else {
                                return new ResponseEntity<>("Language is not valid",HttpStatus.BAD_REQUEST);
                            }
                        }
                    }
                } else if (fData != null && "Video".equalsIgnoreCase(fData)) {
                    Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
                    Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
                    if (commonQRParaIdOptional.isPresent()) {
                        int count = audioService.deleteVideoByCommonId(commonId);
                        if (count>0){
                            return new ResponseEntity<>("Video is deleted.", HttpStatus.OK);
                        }else {
                            return new ResponseEntity<>("Video is not deleted", HttpStatus.BAD_REQUEST);
                        }
                    } else if (subComIdOptional.isPresent()) {
                        int count = audioService.deleteVideoByCommonId(commonId);
                        if (count>0){
                            return new ResponseEntity<>("Video is deleted.", HttpStatus.OK);
                        }else {
                            return new ResponseEntity<>("Video is not deleted", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>("CommonId is not valid", HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }catch(Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteSingleItem/{commonId}")
    public ResponseEntity<?>deleteSingleItem(@PathVariable String commonId,@RequestParam Integer mtId,
                                             @RequestParam Integer dataType,@RequestParam Integer itemId){
        try {
            Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByCommonId(commonId);
            Optional<SubComId>subComIdOptional=subComIdRepo.findByFsCommonId(commonId);
            if (commonQRParaIdOptional.isPresent()){
                CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                String malId = commonQRParaId.getMalId();
                String engId = commonQRParaId.getEngId();
                Optional<DataType>dataTypeOptional = dataTypeRepo.findById(dataType);
                if (dataTypeOptional.isPresent()){
                    DataType dataType1 = dataTypeOptional.get();
                    String talk = dataType1.getTalk();
                    if ("Malayalam".equalsIgnoreCase(talk)){
                        Optional<FileType>fileTypeOptional=fileTypeRepo.findById(mtId);
                        if (fileTypeOptional.isPresent()){
                            FileType fileType = fileTypeOptional.get();
                            String type=fileType.getFileType();
                            if ("Audio".equalsIgnoreCase(type)){
                                return audioService.deleteAudioSingleItem(malId,itemId);
                            } else if ("Video".equalsIgnoreCase(type)) {
                                return audioService.deleteVideoSingleItem(commonId,itemId);
                            } else if ("Pdf".equalsIgnoreCase(type)) {
                                return audioService.deletePdfSingleItem(malId,itemId);
                            }else {
                                return new ResponseEntity<>("File Type is not valid : "+type,HttpStatus.BAD_REQUEST);
                            }
                        }return new ResponseEntity<>("File type is not valid : "+mtId,HttpStatus.NOT_FOUND);
                    } else if ("English".equalsIgnoreCase(talk)) {
                        Optional<FileType>fileTypeOptional=fileTypeRepo.findById(mtId);
                        if (fileTypeOptional.isPresent()){
                            FileType fileType = fileTypeOptional.get();
                            String type=fileType.getFileType();
                            if ("Audio".equalsIgnoreCase(type)){
                                return audioService.deleteAudioSingleItem(engId,itemId);
                            } else if ("Video".equalsIgnoreCase(type)) {
                                return audioService.deleteVideoSingleItem(commonId,itemId);
                            } else if ("Pdf".equalsIgnoreCase(type)) {
                                return audioService.deletePdfSingleItem(engId,itemId);
                            }else {
                                return new ResponseEntity<>("File Type is not valid : "+type,HttpStatus.BAD_REQUEST);
                            }
                        }return new ResponseEntity<>("File type is not valid : "+mtId,HttpStatus.NOT_FOUND);
                    }else {
                        return new ResponseEntity<>("Language is not present",HttpStatus.BAD_REQUEST);
                    }
                }return new ResponseEntity<>("Language Type is not valid : "+dataType,HttpStatus.NOT_FOUND);
            } else if (subComIdOptional.isPresent()) {
                SubComId subComId = subComIdOptional.get();
                String malId = subComId.getFsMalId();
                String engId = subComId.getFsEngId();
                Optional<DataType>dataTypeOptional = dataTypeRepo.findById(dataType);
                if (dataTypeOptional.isPresent()){
                    DataType dataType1 = dataTypeOptional.get();
                    String talk = dataType1.getTalk();
                    if ("Malayalam".equalsIgnoreCase(talk)){
                        Optional<FileType>fileTypeOptional=fileTypeRepo.findById(mtId);
                        if (fileTypeOptional.isPresent()){
                            FileType fileType = fileTypeOptional.get();
                            String type=fileType.getFileType();
                            if ("Audio".equalsIgnoreCase(type)){
                                return audioService.deleteAudioSingleItem(malId,itemId);
                            } else if ("Video".equalsIgnoreCase(type)) {
                                return audioService.deleteVideoSingleItem(commonId,itemId);
                            } else {
                                return new ResponseEntity<>("File Type is not valid : "+type,HttpStatus.BAD_REQUEST);
                            }
                        }return new ResponseEntity<>("File type is not valid : "+mtId,HttpStatus.NOT_FOUND);
                    } else if ("English".equalsIgnoreCase(talk)) {
                        Optional<FileType>fileTypeOptional=fileTypeRepo.findById(mtId);
                        if (fileTypeOptional.isPresent()){
                            FileType fileType = fileTypeOptional.get();
                            String type=fileType.getFileType();
                            if ("Audio".equalsIgnoreCase(type)){
                                return audioService.deleteAudioSingleItem(engId,itemId);
                            } else if ("Video".equalsIgnoreCase(type)) {
                                return audioService.deleteVideoSingleItem(commonId,itemId);
                            } else {
                                return new ResponseEntity<>("File Type is not valid : "+type,HttpStatus.BAD_REQUEST);
                            }
                        }return new ResponseEntity<>("File type is not valid : "+mtId,HttpStatus.NOT_FOUND);
                    }else {
                        return new ResponseEntity<>("Language is not present",HttpStatus.BAD_REQUEST);
                    }
                }return new ResponseEntity<>("Language Type is not valid : "+dataType,HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<>("CommonId : "+commonId,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
}
