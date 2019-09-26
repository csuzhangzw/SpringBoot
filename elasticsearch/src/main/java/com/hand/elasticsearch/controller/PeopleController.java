package com.hand.elasticsearch.controller;

import com.hand.elasticsearch.entity.People;
import com.hand.elasticsearch.entity.ResponseData;
import com.hand.elasticsearch.service.IUserService;
import com.hand.elasticsearch.utils.DateTimeUtil;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class PeopleController {

    private static Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private IUserService userService;

    /**
     * 同步方式发送获取文档请求
     *
     * @param id 文档ID
     * @param loginName 用户名
     * @param password 密码
     * @return 响应结果
     */
    @GetMapping("api/people/get")
    @ResponseBody
    public ResponseEntity get(@RequestParam(name = "id") String id,
                              @RequestParam(name = "loginName") String loginName,
                              @RequestParam(name = "password") String password) throws Exception {

        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
            throw new Exception("用户名或密码不能为空！");
        } else {
            ResponseData responseData = userService.validateUser(loginName, password);
            if (!responseData.isSuccess()) {
                throw new Exception("用户名或密码不正确！");
            }
        }

        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        try {

            GetRequest request = new GetRequest("people", id);
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            if (null != response && response.isExists()) {
                return new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (IOException e) {
            logger.error("获取文档异常", e);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * 同步方式发送获取文档请求
     *
     * @param id 文档ID
     * @return 响应结果
     */
    @GetMapping("/people/get")
    @ResponseBody
    public ResponseEntity get(@RequestParam(name = "id") String id) {

        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        try {

            GetRequest request = new GetRequest("people", id);
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            if (null != response && response.isExists()) {
                return new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (IOException e) {
            logger.error("获取文档异常", e);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * 异步方式发送获取文档请求
     *
     * @param id 文档ID
     * @return 响应结果
     */
    @GetMapping("/people/getAsync")
    @ResponseBody
    public ResponseEntity getAsync(@RequestParam(name = "id") String id) {

        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        try {

            GetRequest request = new GetRequest("people", id);

            ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
                @Override
                public void onResponse(GetResponse getResponse) {
                    //请求成功回调函数
                    System.out.println(getResponse);
                }

                @Override
                public void onFailure(Exception e) {
                    //请求失败回调函数
                    logger.error("获取文档异常", e);
                }
            };
            client.getAsync(request, RequestOptions.DEFAULT, listener);
        } catch (Exception e) {
            logger.error("获取文档异常", e);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * 新增文档内容
     *
     * @param people 对象实体
     * @return 响应结果
     */
    @PostMapping("/people/add")
    @ResponseBody
    public ResponseEntity add(@RequestBody People people) {

        try {

            IndexRequest request = new IndexRequest("people");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("name", people.getName());
            builder.field("country", people.getCountry());
            builder.field("age", people.getAge());
            builder.field("date", formatter.format(people.getDate()));
            builder.endObject();
            request.source(builder);

            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            if (null != indexResponse && indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                return new ResponseEntity(indexResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("新增文档异常", e);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * 批量操作文档内容
     *
     * @param peopleList 对象实体集合
     * @return 响应结果
     */
    @PostMapping("/people/batch/update")
    @ResponseBody
    public ResponseEntity batchUpdate(@RequestBody List<People> peopleList) {

        try {

            BulkRequest request = new BulkRequest();

            //批量操作
            for (People people : peopleList) {
                if ("add".equals(people.get_status())) {
                    //_id由elasticsearch自动生成
                    request.add(new IndexRequest("people")
                            .source(XContentType.JSON, "name", people.getName(),
                                    "country", people.getCountry(),
                                    "age", people.getAge(),
                                    "date", DateTimeUtil.date2String(people.getDate(), DateTimeUtil.FORMAT_SHORT)));
                } else {
                    if (!StringUtils.isEmpty(people.get_id())) {
                        if ("delete".equals(people.get_status())) {
                            //删除
                            request.add(new DeleteRequest("people", people.get_id()));
                        } else {
                            //更新
                            request.add(new UpdateRequest("people", people.get_id())
                                    .doc(XContentType.JSON, "name", people.getName(),
                                            "country", people.getCountry(),
                                            "age", people.getAge(),
                                            "date", DateTimeUtil.date2String(people.getDate(), DateTimeUtil.FORMAT_SHORT)));
                        }
                    } else {
                        logger.error("更新或删除时_id不能为空");
                    }
                }
            }

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            if (null != bulkResponse) {

                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        System.out.println(indexResponse);
                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        System.out.println(updateResponse);
                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                        System.out.println(deleteResponse);                    }
                }

                return new ResponseEntity(bulkResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("批量处理文档异常", e);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
