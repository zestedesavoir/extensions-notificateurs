//
//  ZDSAppDelegate.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "ParserZSD.h"

@interface ZDSAppDelegate : NSObject <NSApplicationDelegate,NSUserNotificationCenterDelegate>{
    NSMutableArray *_allNotification;
    
    IBOutlet NSMenu *statusMenu;
    
    NSStatusItem *statusItem;
}

@property (assign) IBOutlet NSWindow *window;
- (NSUserNotification *) showNotficationWithTitle:(NSString *)title AndWithInformation :(NSString *)infomation withImageAuteurs: (NSString *)url;
- (void) checkNew;
-(BOOL)findArrayInArray:(NSMutableArray *)arrayInArray withArray:(NSMutableArray *)arrayValue;
@end
