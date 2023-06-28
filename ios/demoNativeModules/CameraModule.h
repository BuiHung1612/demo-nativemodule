//
//  CameraModule.h
//  demoNativeModules
//
//  Created by User on 09/02/2023.
//

#import <React/RCTBridgeModule.h>
#import <UIKit/UIKit.h>

@interface CameraModule : NSObject <RCTBridgeModule, UIImagePickerControllerDelegate, UINavigationControllerDelegate>
@property RCTResponseSenderBlock imageCallBack;
@end
