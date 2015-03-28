//
//  CPRofileAll.h
//  Clem
//
//  Created by Odric Roux-Paris on 24/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef void (^CBlocProgress)(long progressMembreDownload, long totalMembre);
typedef void (^CBlocSuccess)(NSArray *membre, NSError *e);

@interface CPRofileAll : NSObject


@property (nonatomic, copy) CBlocProgress blocProgress;
@property (nonatomic, copy) CBlocSuccess   blocSuccess;

@property (readonly) long totalMembre;
@property (readonly) long currentMembre;
@property (readonly) NSMutableArray *membre;


- (void)getAllMembre:(CBlocProgress)progress sucess:(CBlocSuccess)success;

- (void)sendRequestWithURL:(NSString *)url; //NSString car AFNetworking utilise des NSString à la place de NSURL



+ (instancetype)profileAll;

@end
