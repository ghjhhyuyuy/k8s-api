package team.cloud;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import team.cloud.k8s.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ernest ljw9712@163.com
 * @date 2018/12/27
 */
public class App {

    public static void main( String[] args )
    {
        Fabric8 fabric8 = new Fabric8();
        K8sServices k8sServices = new K8sServices();
        K8sDaemonSets k8sDaemonSets = new K8sDaemonSets();
        K8sNodes k8sNodes = new K8sNodes();
        K8sNamespaces k8sNamespaces = new K8sNamespaces();
        K8sPods k8sPods = new K8sPods();
        K8sDeployments k8sDeployments = new K8sDeployments();
        KubernetesClient client = fabric8.connect();
        K8sConfigMaps k8sConfigMaps = new K8sConfigMaps();

        Map<String, String> labels = new HashMap<String, String>(){
            {
                put("name","ljw");
                put("test","test");
            }
        };
        Map<String, String> labels1 = new HashMap<String, String>(){
            {
                put("app","nginx");
                put("track","stable");
            }
        };
        Map<String, String> labels2 = new HashMap<String, String>(){
            {
                put("app","nginx");
            }
        };
        Map<String, String> annotations= new HashMap<String, String>(){
            {
                put("ljw","18699774010");
                put("test","test");
            }
        };


//        Namespace namespace = new Namespace();
//        ObjectMeta objectMeta = new ObjectMeta();
//        objectMeta.setName("ljwtest");
//        objectMeta.setAnnotations(annotations);
//        objectMeta.setLabels(labels);
//        objectMeta.setLabels(labels);
//        namespace.setMetadata(objectMeta);

        /**
         * daemonSet测试代码
         */

//        DaemonSet daemonSet = new DaemonSet();
//        PodSpec podSpec = new PodSpec();
//        List<Container> containers = new ArrayList();
//        Container container = new Container();
//        container.setName("hostname");
//        container.setImage("nginx");
//        containers.add(container);
//        podSpec.setContainers(containers);
//        ObjectMeta objectMeta = new ObjectMeta();
//        objectMeta.setLabels(labels);
//        objectMeta.setName("lujiawei3");
//        objectMeta.setNamespace("default");
//        PodTemplateSpec podTemplateSpec = new PodTemplateSpec();
//        podTemplateSpec.setMetadata(objectMeta);
//        podTemplateSpec.setSpec(podSpec);
//        LabelSelector labelSelector = new LabelSelector();
//        labelSelector.setMatchLabels(labels);
//        DaemonSetSpec daemonSetSpec = new DaemonSetSpec();
//        daemonSetSpec.setSelector(labelSelector);
//        daemonSetSpec.setTemplate(podTemplateSpec);
//        daemonSet.setSpec(daemonSetSpec);
//        daemonSet.setMetadata(objectMeta);
        /**
         * deployment测试代码
         */
        List<ContainerPort> list = new ArrayList<>();
        ContainerPort containerPort = new ContainerPort();
        containerPort.setContainerPort(6379);
        list.add(containerPort);
        Deployment deployment = new Deployment();
        ObjectMeta objectMeta = new ObjectMeta();
        PodSpec podSpec = new PodSpec();
        List<Container> containers = new ArrayList();
        Container container = new Container();
        container.setName("nginx");
        container.setImage("nginx");
        container.setPorts(list);
        containers.add(container);
        podSpec.setContainers(containers);
        objectMeta.setLabels(labels1);
        objectMeta.setName("lujiawei5");
        objectMeta.setNamespace("default");
        DeploymentSpec deploymentSpec = new DeploymentSpec();
        deploymentSpec.setReplicas(1);
        LabelSelector labelSelector = new LabelSelector();
        labelSelector.setMatchLabels(labels1);
        PodTemplateSpec podTemplateSpec = new PodTemplateSpec();
        podTemplateSpec.setMetadata(objectMeta);
        podTemplateSpec.setSpec(podSpec);
        deploymentSpec.setSelector(labelSelector);
        deploymentSpec.setTemplate(podTemplateSpec);
        deployment.setMetadata(objectMeta);
        deployment.setSpec(deploymentSpec);
        /**
         * service测试代码
         */
//        List<ServicePort> list = new ArrayList<>();
//        ServicePort containerPort = new ServicePort();
//        containerPort.setPort(5000);
//        IntOrString intOrString = new IntOrString(80);
//        containerPort.setTargetPort(intOrString);
//        containerPort.setProtocol("TCP");
//        list.add(containerPort);
//        ObjectMeta objectMeta = new ObjectMeta();
//        objectMeta.setLabels(labels);
//        objectMeta.setName("lujiawei7");
//        objectMeta.setNamespace("default");
//        ServiceSpec serviceSpec = new ServiceSpec();
//        serviceSpec.setPorts(list);
//        serviceSpec.setSelector(labels2);
//        Service service = new Service();
//        service.setMetadata(objectMeta);
//        service.setSpec(serviceSpec);

        System.out.println(k8sNodes.listNodes(client));
        //System.out.println(k8sDaemonSets.createDaemonSet(client,daemonSet));
        //System.out.println(k8sDeployments.createDeployment(client,deployment));
//        System.out.println(k8sConfigMaps.updateAllConfigMaps(client));
        //System.out.println(k8sNodes.getNode(client, "master1"));
        //System.out.println(k8sNamespaces.listNamespaces(client));
        //System.out.println(k8sNamespaces.getNamespace(client, "ljwtest"));
        //System.out.println(k8sNamespaces.createNamespace(client, namespace));
        //System.out.println(k8sNamespaces.deleteNamespace(client, "ljwtest"));
        //System.out.println(k8sPods.listPods(client));
        //System.out.println(k8sPods.listPodsFromNamespace(client, "ingress-nginx"));
        //System.out.println(k8sPods.getPod(client, "default","busybox"));
        //System.out.println(k8sDeployments.listDeployments(client));
        //System.out.println(k8sDeployments.listDeploymentsFromNamespace(client,"default"));
        //System.out.println(k8sDeployments.getDeployment(client, "default","nginx"));
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
