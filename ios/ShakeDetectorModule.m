//
//  ShakeDetectorModule.m
//  demoNativeModules
//
//  Created by User on 14/02/2023.
//
#import "ShakeDetectorModule.h"
#import <UIKit/UIKit.h>
#import <React/RCTEventEmitter.h>

@interface ShakeDetectorModule ()

@end

@implementation ShakeDetectorModule

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return @[@"RCTShowDevMenuNotification"];
}

RCT_EXPORT_METHOD(start: (RCTResponseSenderBlock)callback{
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onMotionEvent:) name:@"RCTShowDevMenuNotification" object:nil];
  callback(@[@(YES)]);
});

RCT_EXPORT_METHOD(stop) {
  [[NSNotificationCenter defaultCenter] removeObserver:self name:@"RCTShowDevMenuNotification" object:nil];
  NSLog(@"End!");

};

- (void)onMotionEvent:(NSNotification *)notification {
    [self sendEventWithName:@"RCTShowDevMenuNotification" body:nil];
}

@end
