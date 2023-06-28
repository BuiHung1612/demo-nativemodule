
//
//  SubclassA.m
//  demoNativeModules
//
//  Created by User on 15/02/2023.
//

#import <Foundation/Foundation.h>
#import "SubclassA.h"
#import <CoreMotion/CoreMotion.h>
#import "ShakeDetectorModule.h"
@implementation SubclassA

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
      [self setBackgroundColor:[UIColor redColor]];
    }
    return self;
}

- (BOOL)canBecomeFirstResponder {
  return YES;
}

- (void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event {
  NSLog(@"[Shake] Got motion: %ld", motion);
  if (motion == UIEventSubtypeMotionShake) {
    NSLog(@"[Shake] Got shake motion");
    [[NSNotificationCenter defaultCenter] postNotificationName:@"A_motion_event" object:nil];
  }
}

@end
