//
//  ICSearchMembre.h
//  Clem
//
//  Created by Odric Roux-Paris on 28/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef void(^CSearchBloc)(NSArray *membre, NSError *error);
@interface CSearchMembre : NSObject

@property NSMutableArray *membre;

@property (copy, nonatomic) CSearchBloc searchBloc;

- (void)searchMembre:(NSString *)m bloc:(CSearchBloc)bloc;
- (void)sendRequestWithURL:(NSString *)url; //NSString car AFNetworking utilise des NSString à la place de NSURL



+ (instancetype)searchMembre;
@end
