package com.wireme.mediaserver;

import android.util.Log;

import com.some.SwitchPower;
import com.some.Util;

import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.NetworkAddress;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import java.util.UUID;

public class MediaServer {

	//private UDN udn = UDN.uniqueSystemIdentifier("GNaP-MediaServer");
    private UDN udn = new UDN(UUID.randomUUID());

	private LocalDevice _medialocalDevice;
    private LocalDevice _lightLocalDevice;

	private final static String deviceType = "MediaServer";
	private final static int version = 1;
	private final static String LOGTAG = Util.TAG+"=GNaP-MediaServer";
	private static NetworkAddress _localAddress;

    // DOC:CREATE_DEVICE
    protected void createLightDevice()
            throws ValidationException, LocalServiceBindingException {

        DeviceType type =
                new UDADeviceType("BinaryLight", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Friendly Binary Light",
                        new ManufacturerDetails("ACME"),
                        new ModelDetails("AndroidLight", "A light with on/off switch.", "v1")
                );

        LocalService service =
                new AnnotationLocalServiceBinder().read(SwitchPower.class);

        service.setManager(
                new DefaultServiceManager<SwitchPower>(service, SwitchPower.class)
        );

        createMediaDevice();

        _lightLocalDevice = new LocalDevice(
                new DeviceIdentity(udn),
                type,
                details,
                //createDefaultDeviceIcon(),
                service,
                _medialocalDevice
        );
    }
    // DOC:CREATE_DEVICE

    private LocalService createAVTransportService() {
        LocalService service = new AnnotationLocalServiceBinder()
                  .read(MyAVTransportService.class);
        service.setManager(
                new DefaultServiceManager(service,
                        MyAVTransportService.class)
        );

        return service;
    }

    private void createMediaDevice() throws ValidationException {
        DeviceType type = new UDADeviceType(deviceType, version);

        DeviceDetails details = new DeviceDetails(android.os.Build.MODEL,
                new ManufacturerDetails(android.os.Build.MANUFACTURER),
                new ModelDetails("GNaP", "GNaP MediaServer for Android", "v1"));

        LocalService service = new AnnotationLocalServiceBinder()
                .read(ContentDirectoryService.class);

        service.setManager(
                new DefaultServiceManager<ContentDirectoryService>(
                        service,
                        ContentDirectoryService.class)
        );

        _medialocalDevice = new LocalDevice(
                new DeviceIdentity(udn),
                type,
                details,
                service
        );

        Log.v(LOGTAG, "MediaServer device created: ");
        Log.v(LOGTAG, "friendly name: " + details.getFriendlyName());
        Log.v(LOGTAG, "manufacturer: "
                + details.getManufacturerDetails().getManufacturer());
        Log.v(LOGTAG, "model: " + details.getModelDetails().getModelName());
    }

	public MediaServer(NetworkAddress localAddress) throws ValidationException {

		_localAddress = localAddress;


		
		/*start http server
		try {
			new HttpServer(port);
		}
		catch (IOException ioe )
		{
			System.err.println( "Couldn't start server:\n" + ioe );
			System.exit( -1 );
		}
		
		Log.v(LOGTAG, "Started Http Server on port " + port);
		*/
	}

	public LocalDevice getDevice() throws ValidationException {
        if (_medialocalDevice == null)
            createMediaDevice();
		return _medialocalDevice;
	}

	public String getAddress() {
		return _localAddress.getAddress().getHostAddress() + ":" + _localAddress.getPort();
	}
}
