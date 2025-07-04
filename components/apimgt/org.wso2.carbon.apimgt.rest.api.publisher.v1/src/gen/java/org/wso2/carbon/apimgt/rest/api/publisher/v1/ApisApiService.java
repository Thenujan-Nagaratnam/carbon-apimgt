package org.wso2.carbon.apimgt.rest.api.publisher.v1;

import org.wso2.carbon.apimgt.rest.api.publisher.v1.*;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.*;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.apimgt.api.APIManagementException;

import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIEndpointDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIEndpointListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIExternalStoreListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIKeyDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIMonetizationInfoDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIRevenueDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIRevisionDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIRevisionDeploymentDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIRevisionDeploymentListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.APIRevisionListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ApiEndpointValidationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.AsyncAPISpecificationValidationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.AuditReportDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.CertificateInfoDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ClientCertMetadataDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ClientCertificatesDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.CommentDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.CommentListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ContentPublishStatusDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ContentPublishStatusResponseDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.DocumentDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.DocumentListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ErrorDTO;
import java.io.File;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.FileInfoDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.GraphQLQueryComplexityInfoDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.GraphQLSchemaDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.GraphQLSchemaTypeListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.GraphQLValidationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.LabelListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.LifecycleHistoryDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.LifecycleStateDTO;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.MockResponsePayloadListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OpenAPIDefinitionValidationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.PatchRequestBodyDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.PostRequestBodyDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.RequestLabelListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ResourcePathListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ResourcePolicyInfoDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ResourcePolicyListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.SequenceBackendListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ThrottlingPolicyDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.TopicListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.WSDLInfoDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.WSDLValidationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.WorkflowResponseDTO;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface ApisApiService {
      public Response addAPIClientCertificate(String apiId, InputStream certificateInputStream, Attachment certificateDetail, String alias, String tier, MessageContext messageContext) throws APIManagementException;
      public Response addAPIClientCertificateOfGivenKeyType(String keyType, String apiId, InputStream certificateInputStream, Attachment certificateDetail, String alias, String tier, MessageContext messageContext) throws APIManagementException;
      public Response addAPIDocument(String apiId, DocumentDTO documentDTO, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response addAPIDocumentContent(String apiId, String documentId, String ifMatch, InputStream fileInputStream, Attachment fileDetail, String inlineContent, MessageContext messageContext) throws APIManagementException;
      public Response addAPIMonetization(String apiId, APIMonetizationInfoDTO apIMonetizationInfoDTO, MessageContext messageContext) throws APIManagementException;
      public Response addAPISpecificOperationPolicy(String apiId, InputStream policySpecFileInputStream, Attachment policySpecFileDetail, InputStream synapsePolicyDefinitionFileInputStream, Attachment synapsePolicyDefinitionFileDetail, InputStream ccPolicyDefinitionFileInputStream, Attachment ccPolicyDefinitionFileDetail, MessageContext messageContext) throws APIManagementException;
      public Response addApiEndpoint(String apiId, APIEndpointDTO apIEndpointDTO, MessageContext messageContext) throws APIManagementException;
      public Response addCommentToAPI(String apiId, PostRequestBodyDTO postRequestBodyDTO, String replyTo, MessageContext messageContext) throws APIManagementException;
      public Response apisApiIdAsyncapiGet(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response apisApiIdAsyncapiPut(String apiId, String ifMatch, String apiDefinition, String url, InputStream fileInputStream, Attachment fileDetail, MessageContext messageContext) throws APIManagementException;
      public Response apisApiIdEnvironmentsEnvIdKeysGet(String apiId, String envId, MessageContext messageContext) throws APIManagementException;
      public Response apisApiIdEnvironmentsEnvIdKeysPut(String apiId, String envId, Map<String, String> requestBody, MessageContext messageContext) throws APIManagementException;
      public Response attachLabelsToAPI(String apiId, RequestLabelListDTO requestLabelListDTO, MessageContext messageContext) throws APIManagementException;
      public Response changeAPILifecycle(String action, String apiId, String lifecycleChecklist, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response createAPI(APIDTO APIDTO, String openAPIVersion, MessageContext messageContext) throws APIManagementException;
      public Response createAPIRevision(String apiId, APIRevisionDTO apIRevisionDTO, MessageContext messageContext) throws APIManagementException;
      public Response createNewAPIVersion(String newVersion, String apiId, Boolean defaultVersion, String serviceVersion, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPI(String apiId, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPIClientCertificateByAlias(String alias, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPIClientCertificateByKeyTypeAndAlias(String keyType, String alias, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPIDocument(String apiId, String documentId, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPILifecycleStatePendingTasks(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPIRevision(String apiId, String revisionId, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPIRevisionDeploymentPendingTask(String apiId, String revisionId, String envName, MessageContext messageContext) throws APIManagementException;
      public Response deleteAPISpecificOperationPolicyByPolicyId(String apiId, String operationPolicyId, MessageContext messageContext) throws APIManagementException;
      public Response deleteApiEndpoint(String apiId, String endpointId, MessageContext messageContext) throws APIManagementException;
      public Response deleteApiTheme(String apiId, String id, MessageContext messageContext) throws APIManagementException;
      public Response deleteComment(String commentId, String apiId, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response deployAPIRevision(String apiId, String revisionId, List<APIRevisionDeploymentDTO> apIRevisionDeploymentDTO, MessageContext messageContext) throws APIManagementException;
      public Response detachLabelsFromAPI(String apiId, RequestLabelListDTO requestLabelListDTO, MessageContext messageContext) throws APIManagementException;
      public Response editCommentOfAPI(String commentId, String apiId, PatchRequestBodyDTO patchRequestBodyDTO, MessageContext messageContext) throws APIManagementException;
      public Response exportAPI(String apiId, String name, String version, String revisionNumber, String providerName, String format, Boolean preserveStatus, Boolean latestRevision, String gatewayEnvironment, Boolean preserveCredentials, MessageContext messageContext) throws APIManagementException;
      public Response generateInternalAPIKey(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response generateMockScripts(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPI(String apiId, String xWSO2Tenant, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIClientCertificateByAlias(String alias, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getAPIClientCertificateByKeyTypeAndAlias(String keyType, String alias, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getAPIClientCertificateContentByAlias(String apiId, String alias, MessageContext messageContext) throws APIManagementException;
      public Response getAPIClientCertificateContentByKeyTypeAndAlias(String apiId, String alias, String keyType, MessageContext messageContext) throws APIManagementException;
      public Response getAPIClientCertificates(String apiId, Integer limit, Integer offset, String alias, MessageContext messageContext) throws APIManagementException;
      public Response getAPIClientCertificatesByKeyType(String keyType, String apiId, Integer limit, Integer offset, String alias, MessageContext messageContext) throws APIManagementException;
      public Response getAPIDocumentByDocumentId(String apiId, String documentId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIDocumentContentByDocumentId(String apiId, String documentId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIDocuments(String apiId, Integer limit, Integer offset, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIGraphQLSchema(String apiId, String accept, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPILifecycleHistory(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPILifecycleState(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIMonetization(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getAPIResourcePaths(String apiId, Integer limit, Integer offset, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIResourcePolicies(String apiId, String sequenceType, String resourcePath, String verb, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIResourcePoliciesByPolicyId(String apiId, String resourcePolicyId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIRevenue(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getAPIRevision(String apiId, String revisionId, MessageContext messageContext) throws APIManagementException;
      public Response getAPIRevisionDeployments(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getAPIRevisions(String apiId, String query, MessageContext messageContext) throws APIManagementException;
      public Response getAPISpecificOperationPolicyContentByPolicyId(String apiId, String operationPolicyId, MessageContext messageContext) throws APIManagementException;
      public Response getAPISubscriptionPolicies(String apiId, String xWSO2Tenant, String ifNoneMatch, Boolean isAiApi, String organizationID, MessageContext messageContext) throws APIManagementException;
      public Response getAPISwagger(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAPIThumbnail(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAllAPISpecificOperationPolicies(String apiId, Integer limit, Integer offset, String query, MessageContext messageContext) throws APIManagementException;
      public Response getAllAPIs(Integer limit, Integer offset, String xWSO2Tenant, String query, String ifNoneMatch, String accept, MessageContext messageContext) throws APIManagementException;
      public Response getAllCommentsOfAPI(String apiId, String xWSO2Tenant, Integer limit, Integer offset, Boolean includeCommenterInfo, MessageContext messageContext) throws APIManagementException;
      public Response getAllPublishedExternalStoresByAPI(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getAmazonResourceNamesOfAPI(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getApiEndpoint(String apiId, String endpointId, MessageContext messageContext) throws APIManagementException;
      public Response getApiEndpoints(String apiId, Integer limit, Integer offset, MessageContext messageContext) throws APIManagementException;
      public Response getApiThemeContent(String apiId, String id, MessageContext messageContext) throws APIManagementException;
      public Response getApiThemes(String apiId, Boolean publish, MessageContext messageContext) throws APIManagementException;
      public Response getAuditReportOfAPI(String apiId, String accept, MessageContext messageContext) throws APIManagementException;
      public Response getCommentOfAPI(String commentId, String apiId, String xWSO2Tenant, String ifNoneMatch, Boolean includeCommenterInfo, Integer replyLimit, Integer replyOffset, MessageContext messageContext) throws APIManagementException;
      public Response getGeneratedMockScriptsOfAPI(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response getGraphQLPolicyComplexityOfAPI(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getGraphQLPolicyComplexityTypesOfAPI(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getLabelsOfAPI(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getOperationPolicyForAPIByPolicyId(String apiId, String operationPolicyId, MessageContext messageContext) throws APIManagementException;
      public Response getRepliesOfComment(String commentId, String apiId, String xWSO2Tenant, Integer limit, Integer offset, String ifNoneMatch, Boolean includeCommenterInfo, MessageContext messageContext) throws APIManagementException;
      public Response getSequenceBackendContent(String type, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getSequenceBackendData(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getWSDLInfoOfAPI(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getWSDLOfAPI(String apiId, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response importAPI(InputStream fileInputStream, Attachment fileDetail, Boolean preserveProvider, Boolean rotateRevision, Boolean overwrite, Boolean preservePortalConfigurations, Boolean dryRun, String accept, MessageContext messageContext) throws APIManagementException;
      public Response importApiTheme(String apiId, InputStream fileInputStream, Attachment fileDetail, MessageContext messageContext) throws APIManagementException;
      public Response importAsyncAPISpecification(InputStream fileInputStream, Attachment fileDetail, String url, String additionalProperties, MessageContext messageContext) throws APIManagementException;
      public Response importGraphQLSchema(String ifMatch, String type, InputStream fileInputStream, Attachment fileDetail, String url, String schema, String additionalProperties, MessageContext messageContext) throws APIManagementException;
      public Response importOpenAPIDefinition(InputStream fileInputStream, Attachment fileDetail, String url, String additionalProperties, String inlineAPIDefinition, MessageContext messageContext) throws APIManagementException;
      public Response importServiceFromCatalog(String serviceKey, APIDTO APIDTO, MessageContext messageContext) throws APIManagementException;
      public Response importWSDLDefinition(InputStream fileInputStream, Attachment fileDetail, String url, String additionalProperties, String implementationType, MessageContext messageContext) throws APIManagementException;
      public Response publishAPIToExternalStores(String apiId, String externalStoreIds, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response reimportServiceFromCatalog(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response restoreAPIRevision(String apiId, String revisionId, MessageContext messageContext) throws APIManagementException;
      public Response sequenceBackendDelete(String type, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response sequenceBackendUpdate(String apiId, InputStream sequenceInputStream, Attachment sequenceDetail, String type, MessageContext messageContext) throws APIManagementException;
      public Response undeployAPIRevision(String apiId, String revisionId, String revisionNumber, Boolean allEnvironments, List<APIRevisionDeploymentDTO> apIRevisionDeploymentDTO, MessageContext messageContext) throws APIManagementException;
      public Response updateAPI(String apiId, APIDTO APIDTO, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIClientCertificateByAlias(String alias, String apiId, InputStream certificateInputStream, Attachment certificateDetail, String tier, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIClientCertificateByKeyTypeAndAlias(String keyType, String alias, String apiId, InputStream certificateInputStream, Attachment certificateDetail, String tier, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIDeployment(String apiId, String deploymentId, APIRevisionDeploymentDTO apIRevisionDeploymentDTO, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIDocument(String apiId, String documentId, DocumentDTO documentDTO, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIGraphQLSchema(String apiId, String schemaDefinition, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIResourcePoliciesByPolicyId(String apiId, String resourcePolicyId, ResourcePolicyInfoDTO resourcePolicyInfoDTO, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response updateAPISwagger(String apiId, String ifMatch, String apiDefinition, String url, InputStream fileInputStream, Attachment fileDetail, MessageContext messageContext) throws APIManagementException;
      public Response updateAPIThumbnail(String apiId, InputStream fileInputStream, Attachment fileDetail, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response updateApiEndpoint(String apiId, String endpointId, APIEndpointDTO apIEndpointDTO, MessageContext messageContext) throws APIManagementException;
      public Response updateApiThemeStatus(String apiId, String id, ContentPublishStatusDTO contentPublishStatusDTO, MessageContext messageContext) throws APIManagementException;
      public Response updateGraphQLPolicyComplexityOfAPI(String apiId, GraphQLQueryComplexityInfoDTO graphQLQueryComplexityInfoDTO, MessageContext messageContext) throws APIManagementException;
      public Response updateTopics(String apiId, TopicListDTO topicListDTO, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response updateWSDLOfAPI(String apiId, String ifMatch, InputStream fileInputStream, Attachment fileDetail, String url, MessageContext messageContext) throws APIManagementException;
      public Response validateAPI(String query, String ifNoneMatch, MessageContext messageContext) throws APIManagementException;
      public Response validateAsyncAPISpecification(Boolean returnContent, String url, InputStream fileInputStream, Attachment fileDetail, MessageContext messageContext) throws APIManagementException;
      public Response validateDocument(String apiId, String name, String ifMatch, MessageContext messageContext) throws APIManagementException;
      public Response validateEndpoint(String endpointUrl, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response validateGraphQLSchema(Boolean useIntrospection, InputStream fileInputStream, Attachment fileDetail, String url, MessageContext messageContext) throws APIManagementException;
      public Response validateOpenAPIDefinition(Boolean returnContent, String url, InputStream fileInputStream, Attachment fileDetail, String inlineAPIDefinition, MessageContext messageContext) throws APIManagementException;
      public Response validateWSDLDefinition(String url, InputStream fileInputStream, Attachment fileDetail, MessageContext messageContext) throws APIManagementException;
}
