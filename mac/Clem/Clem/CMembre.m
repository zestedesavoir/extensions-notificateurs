//
//  CMembre.m
//  Clem
//
//  Created by Odric Roux-Paris on 10/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "CMembre.h"
#import "CJSONDeserializer.h"
#import "CJSONSerializer.h"
#import "NSURLComponents+ZDSUrlAvatar.h"
#import "CProfile.h"

@implementation CMembre

@synthesize active;
@synthesize urlAvatar;
@synthesize biography;
@synthesize dateJoined;
@synthesize email;
@synthesize emailForAnswer;
@synthesize lastVisit;
@synthesize pk;
@synthesize showEmail;
@synthesize sign;
@synthesize site;
@synthesize username;

@synthesize blocUpdate;

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
           email:(NSString *)m{
    
    if (self){
        pk = PK;
        username = name;
        showEmail = showMail;
        urlAvatar = iAvatar;
        biography = bio;
        sign = signn;
        emailForAnswer = answer;
        lastVisit = visit;
        dateJoined = date;
        email = m;
    }
    return self;
}

- (BOOL)isEqualToMembre:(CMembre *)m{
    return m.pk == self.pk;
}

- (NSString *)description{
    return [NSString stringWithFormat:@"Membre : %@, pk : %d", username, pk];
}

- (void)updateMembre:(CUpdateMembre)bloc{
    blocUpdate = [bloc copy];
    
    [[CProfile profile] checkProfileWithPK:self.pk bloc:^(CMembre *membre, NSError *error) {
        blocUpdate(error);
        if (membre){
            self.showEmail = membre.showEmail;
            self.urlAvatar = membre.urlAvatar;
            self.sign = membre.sign;
            self.site = membre.site;
            self.biography = membre.biography;
            self.email = membre.email;
            self.emailForAnswer = membre.emailForAnswer;
            self.lastVisit = membre.lastVisit;
            
        }
    }];
}

+ (CMembre *)jsonToMembre:(NSString *)json{
    NSError *error;
    NSDictionary *dicoJson = [[CJSONDeserializer deserializer] deserializeAsDictionary:[json dataUsingEncoding:NSUTF8StringEncoding]
                                                                                 error:&error];
    
    NSDateFormatter *f = [[NSDateFormatter alloc] init];
    [f setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];

   
    
    
    CMembre *membre = [[CMembre alloc] init];
    [membre setPk:[dicoJson[@"pk"] intValue]];
    [membre setUsername:dicoJson[@"username"]];
    [membre setShowEmail:[dicoJson[@"show_email"] boolValue]];
    [membre setEmail:dicoJson[@"email"]];
    [membre setActive:[dicoJson[@"is_active"] boolValue]];
    [membre setSite:[NSURL URLWithString:dicoJson[@"site"]]];
    [membre setBiography:dicoJson[@"biography"]];
    [membre setSign:dicoJson[@"sign"]];
    [membre setEmailForAnswer:[dicoJson[@"email_for_answer"] boolValue]];
    [membre setLastVisit:[f dateFromString:dicoJson[@"last_visit"]]];
    [membre setDateJoined:[f dateFromString:dicoJson[@"date_joined"]]];
    
    
    if (!dicoJson[@"avatar_url"] ){
        NSURLComponents *urlComponents = [NSURLComponents componentsWithString:dicoJson[@"avatar_url"]];
        [urlComponents ZDSrectifiUrlAvatar];
        NSURL *urlAvatar = [urlComponents URL];
        [membre setUrlAvatar:urlAvatar];
    }

    
    return membre;
}
+ (CMembre *)dictionnaryToMembre:(NSDictionary *)dico{
    NSDateFormatter *f = [[NSDateFormatter alloc] init];
    [f setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];
    
    
    
    CMembre *membre = [[CMembre alloc] init];
    
    
    [membre setPk:[dico[@"pk"] intValue]];
    [membre setUsername:dico[@"username"]];
    [membre setShowEmail:[dico[@"show_email"] boolValue]];
    [membre setEmail:dico[@"email"]];
    [membre setActive:[dico[@"is_active"] boolValue]];
    [membre setSite:[NSURL URLWithString:dico[@"site"]]];
    [membre setBiography:dico[@"biography"]];
    [membre setSign:dico[@"sign"]];
    [membre setEmailForAnswer:[dico[@"email_for_answer"] boolValue]];
    [membre setLastVisit:[f dateFromString:dico[@"last_visit"]]];
    [membre setDateJoined:[f dateFromString:dico[@"date_joined"]]];
    
    
    if (!dico[@"avatar_url"] ){
        NSURLComponents *urlComponents = [NSURLComponents componentsWithString:dico[@"avatar_url"]];
        [urlComponents ZDSrectifiUrlAvatar];
        NSURL *urlAvatar = [urlComponents URL];
        [membre setUrlAvatar:urlAvatar];
    }

    
    return membre;
}

+ (NSString *)membreToJson:(CMembre *)m{
    NSError *error;
    NSDateFormatter *f = [[NSDateFormatter alloc] init];
    [f setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];
    
    NSMutableDictionary *dico = [NSMutableDictionary dictionary];
    dico[@"pk"] = [NSNumber numberWithInt:m.pk];
    dico[@"username"] = m.username;
    dico[@"show_email"] = [NSNumber numberWithBool:m.showEmail];
    dico[@"email"] = m.email;
    dico[@"is_active"] = [NSNumber numberWithBool:m.active];
    dico[@"site"] = m.site.absoluteString;
    dico[@"biography"] = m.biography;
    dico[@"sign"] = m.sign;
    dico[@"email_for_answer"] = [NSNumber numberWithBool:m.emailForAnswer];
    dico[@"last_visit"] = [f stringFromDate:m.lastVisit];
    dico[@"date_joined"] = [f stringFromDate:m.dateJoined];
    NSData *dataMembre = [[CJSONSerializer serializer] serializeDictionary:dico error:&error];
    
    
    
    return [[NSString alloc] initWithData:dataMembre encoding:NSUTF8StringEncoding];
}

+ (NSDictionary *)membreToDictionnary:(CMembre *)m{
    NSDateFormatter *f = [[NSDateFormatter alloc] init];
    [f setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];
    
    NSMutableDictionary *dico = [NSMutableDictionary dictionary];
    dico[@"pk"] = [NSNumber numberWithInt:m.pk];
    dico[@"username"] = m.username;
    dico[@"show_email"] = [NSNumber numberWithBool:m.showEmail];
    dico[@"email"] = m.email;
    dico[@"is_active"] = [NSNumber numberWithBool:m.active];
    dico[@"site"] = m.site.absoluteString;
    dico[@"biography"] = m.biography;
    dico[@"sign"] = m.sign;
    dico[@"email_for_answer"] = [NSNumber numberWithBool:m.emailForAnswer];
    dico[@"last_visit"] = [f stringFromDate:m.lastVisit];
    dico[@"date_joined"] = [f stringFromDate:m.dateJoined];
    
    return [dico copy];
}


                                                                    

@end
