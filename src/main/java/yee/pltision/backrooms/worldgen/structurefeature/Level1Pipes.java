package yee.pltision.backrooms.worldgen.structurefeature;

//public class Level1Pipes extends StructureFeature<NoneFeatureConfiguration> {
//    public Level1Pipes(Codec<NoneFeatureConfiguration> p_197165_, PieceGeneratorSupplier<NoneFeatureConfiguration> p_197166_) {
//        super(p_197165_, Level1Pipes::generatePieces);
//    }
//
//    private static @NotNull Optional<PieceGenerator<NoneFeatureConfiguration>> generatePieces(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
//        context.chunkGenerator().getBaseHeight();
//
//    }
//
//    public static void addPieces(StructureManager p_162435_, BlockPos p_162436_, Rotation p_162437_, StructurePieceAccessor p_162438_, Random p_162439_) {
//        if (p_162439_.nextDouble() < 0.5D) {
//            int i = p_162439_.nextInt(8) + 4;
//            p_162438_.addPiece(new IglooPieces.IglooPiece(p_162435_, STRUCTURE_LOCATION_LABORATORY, p_162436_, p_162437_, i * 3));
//
//            for(int j = 0; j < i - 1; ++j) {
//                p_162438_.addPiece(new IglooPieces.IglooPiece(p_162435_, STRUCTURE_LOCATION_LADDER, p_162436_, p_162437_, j * 3));
//            }
//        }
//
//        p_162438_.addPiece(new IglooPieces.IglooPiece(p_162435_, STRUCTURE_LOCATION_IGLOO, p_162436_, p_162437_, 0));
//    }
//    public static class PipePiece extends TemplateStructurePiece{
//
//        public PipePiece(StructurePieceType p_210083_, int p_210084_, StructureManager manager, ResourceLocation location, String p_210087_, StructurePlaceSettings p_210088_, BlockPos pos) {
//            super(p_210083_, 0, manager, location, location.toString(), new StructurePlaceSettings(), pos);
//        }
//
//        @Override
//        protected void handleDataMarker(String p_73683_, BlockPos p_73684_, ServerLevelAccessor p_73685_, Random p_73686_, BoundingBox p_73687_) {
//
//        }
//
//        @Override
//        public void postProcess(WorldGenLevel p_192682_, StructureFeatureManager p_192683_, ChunkGenerator p_192684_, Random p_192685_, BoundingBox p_192686_, ChunkPos p_192687_, BlockPos p_192688_) {
//            super.postProcess(p_192682_, p_192683_, p_192684_, p_192685_, p_192686_, p_192687_, p_192688_);
//        }
//    }
//
//
//}
