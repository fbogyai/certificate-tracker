package org.jboss.certificate.tracker.extension;

import java.util.List;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.server.Services;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;

public class PKIClientAddHandler extends AbstractAddStepHandler {

    public static PKIClientAddHandler INSTANCE = new PKIClientAddHandler();

    private PKIClientAddHandler() {

    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        for (AttributeDefinition attr : PKIClientDefinition.ALL_ATTRIBUTES) {
            attr.validateAndSet(operation, model);
        }
    }

    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
            final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        String name = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.ADDRESS)).getLastElement().getValue();
        String url = PKIClientDefinition.URL.resolveModelAttribute(context, model).asString();
        long timeInterval = PKIClientDefinition.TIME_INTERVAL.resolveModelAttribute(context, model).asLong();

        ModelNode truststoreNode = PKIClientDefinition.TRUSTSTORE_NAME.resolveModelAttribute(context, model);
        String truststoreName = truststoreNode.isDefined() ? truststoreNode.asString() : null;
        ModelNode modulNode = PKIClientDefinition.MODULE.resolveModelAttribute(context, model);
        String module = modulNode.isDefined() ? modulNode.asString() : null;

        final ManagementService serverControllerService = new ManagementService();
        ServiceController<ManagementService> serverServiceController = context.getServiceTarget()
                .addService(ManagementService.getServiceName(), serverControllerService)
                .addDependency(Services.JBOSS_SERVER_CONTROLLER, ModelController.class, serverControllerService.modelController)
                .setInitialMode(Mode.ACTIVE)
                .install();
        newControllers.add(serverServiceController);

        CertificateTrackerLogger.LOGGER.addingPKIClient(name, url, Long.toString(timeInterval));

        CertificateTrackingService certificateTrackingService = new CertificateTrackingService(url, truststoreName, timeInterval, name,
                module);
        ServiceName serviceName = CertificateTrackingService.getServiceName();
        ServiceController<CertificateTrackingService> serviceController = context.getServiceTarget()
                .addService(serviceName, certificateTrackingService)
                .addListener(verificationHandler)
                .setInitialMode(Mode.ACTIVE)
                .install();
        newControllers.add(serviceController);
    }

}
