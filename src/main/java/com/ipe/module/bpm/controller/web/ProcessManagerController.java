package com.ipe.module.bpm.controller.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ipe.module.bpm.service.ProcessManagerService;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-18
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("bpm")
public class ProcessManagerController extends AbstractController {
    @Autowired
    private ProcessManagerService processManagerService;

    @Value("#{app.temp_filepath}")
    private String tempFilePath;

    /**
     * 部署流程
     * @param multipartHttpServletRequest
     * @param response
     */
    @RequestMapping("/deploy")
    public void deployProcess(MultipartHttpServletRequest multipartHttpServletRequest,
                              HttpServletResponse response) {
        try {
            MultipartFile multipartFile = multipartHttpServletRequest.getMultiFileMap().getFirst("file");
            File file = new File(tempFilePath + multipartFile.getOriginalFilename());
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            IOUtils.closeQuietly(multipartFile.getInputStream());
            processManagerService.deploy(file.getPath());
            super.renderSuccess(response);
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
            super.renderFailure("部署失败", response);
        }
    }

    /**
     * 测试启动流程
     * @param key
     * @return
     */
    @RequestMapping("/teststartProcess")
    public
    @ResponseBody
    BodyWrapper teststartProcess(@RequestParam String key) {
        try {
            processManagerService.testStartProcess(key);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
            return failure();
        }
    }


    /**
     * 查看流程图
     *
     * @param key
     * @param version
     * @param response
     */
    @RequestMapping("/viewDiagram")
    public void viewDiagram(@RequestParam String key, @RequestParam String version, HttpServletResponse response) {
        response.setContentType("image/png");
        try {
            InputStream inputStream = processManagerService.viewDiagram(key, version);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte data[] = new byte[4096];
            int size = 0;
            size = bis.read(data);
            while (size != -1) {
                bos.write(data, 0, size);
                size = bis.read(data);
            }
            inputStream.close();
            bis.close();
            bos.close();
            outputStream.close();
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
            super.renderFailure("操作失败", response);
        }
    }

    /**
     * 查看运行中流程图
     *
     * @param executionId
     * @param response
     */
    @RequestMapping("/viewDnyDiagram")
    public void viewDiagram(@RequestParam String executionId, HttpServletResponse response) {
        response.setContentType("image/png");
        try {
            InputStream inputStream = processManagerService.viewDnyDiagram(executionId);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte data[] = new byte[4096];
            int size = 0;
            size = bis.read(data);
            while (size != -1) {
                bos.write(data, 0, size);
                size = bis.read(data);
            }
            inputStream.close();
            bis.close();
            bos.close();
            outputStream.close();
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
            super.renderFailure("操作失败", response);
        }
    }

    /**
     * 查看各节点信息
     *
     * @param processInstanceId
     * @return
     */
    @RequestMapping("/viewProcessInfo")
    public
    @ResponseBody
    BodyWrapper viewProcessInfo(@RequestParam String processInstanceId) {
        try {
            List<Map<String, Object>> list = processManagerService.viewProcessInfo(processInstanceId);
            return success(list);
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
            return failure();
        }
    }


    /**
     * 删除流程定义,级联删除流程实例
     *
     * @param deployId
     * @return
     */
    @RequestMapping(value = "/del_def_all")
    public
    @ResponseBody
    BodyWrapper removeAll(@RequestParam String deployId) {
        try {
            processManagerService.delDefAll(deployId);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
            return failure("删除失败");
        }
    }


    /**
     * 流程定义查询
     *
     * @return
     */
    @RequestMapping("/process_def_list")
    public
    @ResponseBody
    BodyWrapper processDefList(RestRequest restRequestt, String params) {
        return processManagerService.proDefList(params, restRequestt);
    }

    /**
     * 流程定义历史
     *
     * @return
     */
    @RequestMapping("/process_def_his_list")
    public
    @ResponseBody
    BodyWrapper processDefHisList(RestRequest restRequest, String params) {
        return processManagerService.proDefHisList(params, restRequest);
    }

    /**
     * 流程实例列表
     *
     * @param restRequest
     * @param params
     * @return
     */
    @RequestMapping("/process_ins_list")
    public
    @ResponseBody
    BodyWrapper processInsList(RestRequest restRequest, String params) {
        return processManagerService.proInstaceList(params, restRequest);
    }

    /**
     * 我发起流程实例列表
     *
     * @param restRequest
     * @param params
     * @return
     */
    @RequestMapping("/my_start_process_ins_list")
    public
    @ResponseBody
    BodyWrapper myStartProcessInsList(RestRequest restRequest, String params) {
        return processManagerService.myStartTask(params, restRequest);
    }

    /**
     * 我参与流程实例列表
     *
     * @param restRequest
     * @param params
     * @return
     */
    @RequestMapping("/my_partake_process_ins_list")
    public
    @ResponseBody
    BodyWrapper myPartakeProcessInsList(RestRequest restRequest, String params) {
        return processManagerService.myPartakeTask(params, restRequest);
    }
}
