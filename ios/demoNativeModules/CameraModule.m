//
//  CameraModule.m
//  demoNativeModules
//
//  Created by User on 09/02/2023.
//

#import <Foundation/Foundation.h>
#import "CameraModule.h"
#import <AVFoundation/AVFoundation.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import <UIKit/UIKit.h>
@implementation CameraModule

RCT_EXPORT_MODULE();
RCTResponseSenderBlock callback;

RCT_EXPORT_METHOD(requestCameraPermission:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  AVAuthorizationStatus authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
  
  if (authStatus == AVAuthorizationStatusAuthorized) {
    resolve(@"authorized");
  } else if (authStatus == AVAuthorizationStatusDenied || authStatus == AVAuthorizationStatusRestricted) {
    reject(@"permission_denied", @"Camera permission is denied or restricted", nil);
  } else if (authStatus == AVAuthorizationStatusNotDetermined) {
    [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
      if (granted) {
        resolve(@"authorized");
      } else {
        reject(@"permission_denied", @"Camera permission is denied", nil);
      }
    }];
  }
}
RCT_EXPORT_METHOD(openCamera:(RCTResponseSenderBlock)callback)
{
  UIImagePickerController *picker = [[UIImagePickerController alloc] init];
  picker.delegate = self;
  picker.sourceType = UIImagePickerControllerSourceTypeCamera;
  picker.allowsEditing = YES;

  UIViewController *root = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
  [root presentViewController:picker animated:YES completion:nil];
  self.imageCallBack = callback;
}


RCT_EXPORT_METHOD(pickImage:(RCTResponseSenderBlock)callback) {
  UIImagePickerController *picker = [[UIImagePickerController alloc] init];
  picker.delegate = self;
  picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
  picker.allowsEditing = NO;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    UIViewController *rootViewController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [rootViewController presentViewController:picker animated:YES completion:nil];
  });

  self.imageCallBack = callback;
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<UIImagePickerControllerInfoKey,id> *)info {
//  NSURL *url = [info objectForKey:UIImagePickerControllerReferenceURL];

  UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
  NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
  NSString *base64String = [imageData base64EncodedStringWithOptions:0];
  self.imageCallBack(@[[NSNull null], base64String]);
  [picker dismissViewControllerAnimated:YES completion:nil];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
  self.imageCallBack(@[@"User cancelled picking image", [NSNull null]]);
}

@end
