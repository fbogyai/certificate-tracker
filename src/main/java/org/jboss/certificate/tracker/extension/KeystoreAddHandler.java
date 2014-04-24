package org.jboss.certificate.tracker.extension;

import java.util.List;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.certificate.tracker.core.KeystoresTrackingManager;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

public class KeystoreAddHandler extends AbstractAddStepHandler {

    public static final KeystoreAddHandler INSTANCE = new KeystoreAddHandler();

    private KeystoreAddHandler() {
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        KeystoreDefinition.PATH.validateAndSet(operation, model);
        KeystoreDefinition.PASSWORD.validateAndSet(operation, model);
        KeystoreDefinition.TYPE.validateAndSet(operation, model);
        KeystoreDefinition.ALIASES.validateAndSet(operation, model);

    }

    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
            final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        String name = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.ADDRESS)).getLastElement().getValue();
        String path = KeystoreDefinition.PATH.resolveModelAttribute(context, model).asString();
        String password = KeystoreDefinition.PASSWORD.resolveModelAttribute(context, model).asString();
        String type = KeystoreDefinition.TYPE.resolveModelAttribute(context, model).asString();

        ModelNode aliasesNode = KeystoreDefinition.ALIASES.resolveModelAttribute(context, model);
        String aliases = aliasesNode.isDefined() ? aliasesNode.toString() : null;

        // add keystore manager to service
        CertificateTrackerLogger.LOGGER.addingNewKeystore(name, path);
        KeystoresTrackingManager.INSTANCE.addKeystore(name, path, type, password, aliases);

    }
}
