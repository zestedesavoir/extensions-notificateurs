//
//  CMembre.h
//  Clem
//
//  Created by Odric Roux-Paris on 10/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>
typedef void(^CUpdateMembre)(NSError *error);
@interface CMembre : NSObject
@property (assign) int pk;
@property          NSString *username;
@property (assign, getter=isShowEmail) BOOL showEmail;
@property (assign, getter=isActive) BOOL active;
@property (strong) NSURL *site;
@property          NSURL *urlAvatar;
@property NSString *biography;
@property NSString *sign;
@property (assign, getter=isEmailForAnswer) BOOL emailForAnswer;
@property (strong) NSDate *lastVisit;
@property (strong) NSDate *dateJoined;
@property (strong) NSString *email;


@property (copy, nonatomic) CUpdateMembre blocUpdate;

- (id)initWithPK:(int)PK
        username:(NSString *)name
     isShowEmail:(BOOL)showMail
            site:(NSURL *)url
     imageAvatar:(NSURL *)iAvatar
       biography:(NSString *)bio
       signature:(NSString *)signn
  emailForAnswer:(BOOL)answer
       lastVisit:(NSDate *)visit
      dateJoined:(NSDate *)date
           email:(NSString *)m;

- (BOOL)isEqualToMembre:(CMembre *)m;
- (void)updateMembre:(CUpdateMembre)bloc;

+ (CMembre *)jsonToMembre:(NSString *)json;
+ (CMembre *)dictionnaryToMembre:(NSDictionary *)dico;
+ (NSString *)membreToJson:(CMembre *)m;
+ (NSDictionary *)membreToDictionnary:(CMembre *)m;


@end
