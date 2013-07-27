package com.model;

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.support.connectionmanager.AbstractPeeringConnectionManagerService;

/**
 * Created by mqddb on 13-7-19.
 */
public class MyDevice extends LocalDevice {

    private DeviceIdentity _identity;
    private DeviceType _type;
    private DeviceDetails _details;
    private LocalService _service;

    public MyDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, LocalService service) throws ValidationException {
        super(identity, type, details, service);
    }

    private LocalService createService() {
        LocalService service;
        service = new AnnotationLocalServiceBinder().read(AbstractPeeringConnectionManagerService.class);
        service.setManager(new DefaultServiceManager(service,AbstractPeeringConnectionManagerService.class));
        return service;
    }
}
