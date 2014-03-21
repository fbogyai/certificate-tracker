package org.jboss.certificate.tracker.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;

public class PKIClientRemoveHandler extends AbstractRemoveStepHandler {

    public static final PKIClientRemoveHandler INSTANCE = new PKIClientRemoveHandler();

    private PKIClientRemoveHandler() {

    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {


        ServiceName name = CertificateTrackingService.getServiceName();
        context.removeService(name);

        ServiceName managementServiceName = ManagementService.getServiceName();
        context.removeService(managementServiceName);
    }
}
