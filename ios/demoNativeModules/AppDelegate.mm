#import "AppDelegate.h"
#import "SubclassA.h"
#import <React/RCTBundleURLProvider.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  self.moduleName = @"demoNativeModules";
//  [application setApplicationSupportsShakeToEdit:YES];
//  AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
//  UIWindow *window = delegate.window;
//
//  // Tạo instance của lớp A
////  SubclassA *aView = [[SubclassA alloc] initWithFrame:self.window.bounds];
//  // Thêm A view vào keyWindow
////  [window insertSubview:aView atIndex:0];
  
  // You can add your custom initial props in the dictionary below.
  // They will be passed down to the ViewController used by React Native.
  self.initialProps = @{};

  BOOL successful = [super application:application didFinishLaunchingWithOptions:launchOptions];
  SubclassA *aView = [[SubclassA alloc] initWithFrame:self.window.bounds];
  [self.window insertSubview:aView atIndex:0];
  return successful;
}

- (void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event {
  NSLog(@"[Shake] Got motion in app delegate: %ld", motion);
  if (motion == UIEventSubtypeMotionShake) {
    NSLog(@"[Shake] Got shake motion in app delegate");
    [[NSNotificationCenter defaultCenter] postNotificationName:@"A_motion_event" object:nil];
  }
}
- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

/// This method controls whether the `concurrentRoot`feature of React18 is turned on or off.
///
/// @see: https://reactjs.org/blog/2022/03/29/react-v18.html
/// @note: This requires to be rendering on Fabric (i.e. on the New Architecture).
/// @return: `true` if the `concurrentRoot` feature is enabled. Otherwise, it returns `false`.
- (BOOL)concurrentRootEnabled
{
  return true;
}

@end
